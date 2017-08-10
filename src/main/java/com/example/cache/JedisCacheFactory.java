package com.example.cache;

import com.example.config.Config;
import com.example.exceptions.ConfigException;
import com.example.interfaces.JComxCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baixiangzhu on 2017/8/8.
 */
public class JedisCacheFactory {

    protected static final String FIELD_ID_CLUSTER = "isCluster";

    private static Map<Config,JComxCache> poolInstance = new HashMap<>();

    public static JComxCache fromConf(Config config) throws ConfigException {

        if(!poolInstance.containsKey(config)){

            poolInstance.put(config,buildJComxCache(config));

        }
        return poolInstance.get(config);
    }

    private static JComxCache buildJComxCache(Config config) throws ConfigException {

        //标记是否集群
        boolean isCluster;

        String isClusterStr = config.str(FIELD_ID_CLUSTER,null);

        if(isClusterStr == null){
            isCluster = true;
        }else {
            isCluster = Boolean.parseBoolean(isClusterStr);
        }

        if(isCluster)
            return new JedisClusterCache(config);

        return new JedisCache(config);
    }


}
