package com.gomeplus.comx.groovytest

import com.example.context.Context;

/**
 * Created by baixiangzhu on 2017/7/26.
 */
/**
 * 过滤不展示字段
 * @param Object
 * @param context
 * @param ref
 */
def callback(Context context,Object data,Map ref){


    data.putAt("groovy","test");

    println("callback execute!")

}
