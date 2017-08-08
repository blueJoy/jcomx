package com.example.cache;

import com.example.config.Config;
import com.example.exceptions.ConfigException;
import redis.clients.jedis.Jedis;
import java.util.Set;

/**
 *   单机版Redis
 *      TODO:是否添加pool
 * Created by baixiangzhu on 2017/8/8.
 */
public class JedisCache extends AbstractJComxCache{


    private Jedis jedis;

    public JedisCache(Config config) throws ConfigException {
        super(config);

        Config redis = config.sub(FIELD_REDIS);
        Config servers = redis.sub(FIELD_SERVERS);

        Set<String> keys = servers.keys();

        for (String key : keys){
            Config sub = servers.sub(key);
            String host = sub.str(FIELD_HOST);
            int port = sub.intValue(FIELD_PORT);

            jedis = new Jedis(host,port);
            break;
        }

    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedis.set(key,value);
    }

    @Override
    public String set(String key, String value, Integer periodTime) {
        return jedis.setex(key,periodTime,value);
    }

}
