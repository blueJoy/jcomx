package com.example.schema;

import com.example.config.Config;

/**
 *  TODO：暂时没什么作用，可考虑去掉
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
