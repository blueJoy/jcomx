package com.example.schema;

import com.example.config.Config;

/**
 * Created by baixiangzhu on 2017/7/27.
 */
public class Schema {

    private Config config;

    public Schema(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
