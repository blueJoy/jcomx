package com.example.cache;

import com.example.config.Config;
import com.example.exceptions.ConfigException;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by baixiangzhu on 2017/8/8.
 */
public class JedisClusterCache extends AbstractJComxCache{

    private JedisCluster jedisCluster;

    public JedisClusterCache(Config config) throws ConfigException {
        super(config);

        Config redis = config.sub(FIELD_REDIS);
        Config servers = redis.sub(FIELD_SERVERS);

        Set<String> keys = servers.keys();

        Set<HostAndPort> hostAndPorts = new HashSet<>();

        for (String key : keys){
            Config sub = servers.sub(key);
            String host = sub.str(FIELD_HOST);
            int port = sub.intValue(FIELD_PORT);

            hostAndPorts.add(new HostAndPort(host,port));
        }

        jedisCluster = new JedisCluster(hostAndPorts);
    }


    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key,value);
    }

    @Override
    public String set(String key, String value, Integer periodTime) {
        return jedisCluster.setex(key,periodTime,value);
    }
}
