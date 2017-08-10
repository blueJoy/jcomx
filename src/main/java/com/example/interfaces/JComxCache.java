package com.example.interfaces;

/**
 *  定义接口，用来限制使用者，可以用到的方法
 * Created by baixiangzhu on 2017/8/8.
 */
public interface JComxCache {

    String get(String  key);

    String set(String key,String value);

    /**
     * 指定失效时间的缓存
     * @param key
     * @param value
     * @param periodTime    单位默认为秒
     * @return
     */
    String set(String key,String value,Integer periodTime);

}
