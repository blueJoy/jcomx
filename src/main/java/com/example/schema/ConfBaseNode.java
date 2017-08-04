package com.example.schema;

import com.example.config.Config;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class ConfBaseNode {

    protected Config config;

   // protected Config parentNode;

    public ConfBaseNode(Config config){
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}
