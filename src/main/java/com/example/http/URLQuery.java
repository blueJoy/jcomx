package com.example.http;

import com.example.common.ArrayAccessBase;

import java.util.HashMap;

/**
 * 存放请求参数的信息
 * Created by baixiangzhu on 2017/7/26.
 */
public class URLQuery implements ArrayAccessBase{

    private String queryString;

    /**
     * 存放请求参数
     */
    private HashMap<String,String> queryParameters;

    public URLQuery(String queryString,HashMap<String,String> queryParameters){
        this.queryString = queryString;
        this.queryParameters = queryParameters;
    }


    /**
     * 获取请求参数的值
     * @param queryKey
     * @return
     */
    public String getParameterValue(String queryKey){

        return queryParameters.get(queryKey);
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return queryParameters.get(key);
    }
}
