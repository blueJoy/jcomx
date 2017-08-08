package com.example.cache;

import com.example.config.Config;

/**
 * Created by baixiangzhu on 2017/8/8.
 */
public abstract class AbstractJComxCache implements JComxCache {

    protected static final String FIELD_REDIS = "redis";
    protected static final String FIELD_SERVERS = "servers";
    protected static final String FIELD_HOST = "host";
    protected static final String FIELD_PORT = "port";

    protected Config config;

    public AbstractJComxCache(Config config){
        this.config = config;
    }

}
