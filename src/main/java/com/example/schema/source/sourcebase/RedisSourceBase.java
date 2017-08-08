package com.example.schema.source.sourcebase;

import com.alibaba.fastjson.JSON;
import com.example.cache.JComxCache;
import com.example.cache.JedisCacheFactory;
import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.SourceException;

import java.io.IOException;
import java.util.HashMap;

/**
 * 获取redis资源数据
 *     直接通过key获取redis中的值
 * Created by baixiangzhu on 2017/8/7.
 */
public class RedisSourceBase extends AbstractSourceBase{

    private JComxCache jComxCache;

    public RedisSourceBase(Config config) throws ConfigException {

        super(config);

        jComxCache = JedisCacheFactory.fromConf(config);
    }

    public JComxCache getJComxCache(){

        return jComxCache;
    }


    @Override
    public Object executeLoading(Context context, Config sourceOptions, HashMap<String, Object> reservedVariables) throws SourceException, ConfigException, IOException {

        String key = (String) reservedVariables.get("reserved_rendered_uri");

        String result = jComxCache.get(key);

        if(result == null)
            throw new SourceException("the key :"+ key +" is not result!");

        try{
            return JSON.parse(result);
        }catch (Exception e){
            throw new SourceException("the result parse json error! result :" + result);
        }
    }
}
