package com.example.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.exceptions.ConfigException;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Set;

/**
 *  存放配置文件的信息
 *          comx.config.jsox/get.json/post.json/put.json/delete.json等
 *
 * Created by baixiangzhu on 2017/7/27.
 */
public class Config {

    private JSONObject dataObject;

    private HashMap<String,Config> subDatas = Maps.newHashMap();

    public Config(JSONObject dataObject){
        this.dataObject = dataObject;
    }


    /**
     * 获取dataObject当前目录层级下的元素的字符串值
     * @param key
     * @return
     */
    public String str(String key) throws ConfigException {
        if(!dataObject.containsKey(key)) throw new ConfigException("Config getStringFromDataObject failed.key:"+key+" config.dataObject="+dataObject);

        Object value = dataObject.get(key);
        if(value instanceof String) return value.toString();
        if(value instanceof Integer) return value.toString();
        if(value instanceof Boolean) return value.toString();

        throw new ConfigException("Config get String type error.key:"+key+" config.dataObject="+dataObject);

    }

    /**
     * 获取json的字符串值
     * @param key
     * @param defaultValue
     * @return
     */
    public String str(String key,String defaultValue){

        try {
            return str(key);
        }catch (ConfigException e){
            return defaultValue;
        }
    }


    public Config sub(String key) throws ConfigException {

        if(!subDatas.containsKey(key)){
            subDatas.put(key,genSub(key,false));
        }

        return subDatas.get(key);
    }

    /**
     *
     * @param key
     * @param restrict  是否限制
     * @return
     * @throws ConfigException
     */
    private Config genSub(String key,boolean restrict) throws ConfigException {

        if(!dataObject.containsKey(key)){
            if(restrict){
                throw new ConfigException("sub node does not exist!key="+key);
            }
            return new Config(new JSONObject());
        }

        Object value = dataObject.get(key);

        if(value instanceof JSONObject){
            return new Config((JSONObject) value);
        }

        //如果是array,则转换成map    key是顺序编号，value是值
        if(value instanceof JSONArray){
            JSONArray array = (JSONArray) value;
            JSONObject tDataObject = new JSONObject();
            for (int i = 0; i < array.size() ; i++){
                tDataObject.put(i+"",array.get(i));
            }
            return new Config(tDataObject);
        }

        throw new ConfigException("type error.expects array or object .key = "+key +"; value="+value);
    }

    public Set<String> keys() {
        return dataObject.keySet();
    }

    public int intValue(String key, Integer defaultValue) {

        try {
            return intValue(key);
        } catch (ConfigException e) {
           return defaultValue;
        }
    }

    public int intValue(String key) throws ConfigException {

        if(!dataObject.containsKey(key)) throw new ConfigException("Config getStringFromDataObject failed.key:"+key+" config.dataObject="+dataObject);

        Object value = dataObject.get(key);
        if(value instanceof Boolean) return (boolean)value?1:0;
        if(value instanceof Integer) return (int)value;
        if(value instanceof String){
            try {
                return Integer.parseInt(String.valueOf(value));
            }catch (NumberFormatException e){
                throw new ConfigException("Config get int type error(convert string to int).key:"+key+" config.dataObject="+dataObject);
            }
        }


        throw new ConfigException("Config get int type error.key:"+key+" config.dataObject="+dataObject);

    }

    public JSONObject getData() {
        return this.dataObject;
    }
}
