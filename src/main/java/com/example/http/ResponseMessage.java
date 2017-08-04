package com.example.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 封装返回的信息
 * Created by baixiangzhu on 2017/7/27.
 */
public class ResponseMessage {

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回错误信息
     */
    private Object error;

    /**
     * 返回debug日志信息
     */
    private JSONArray debug;

    /**
     * 后台返回的真实错误信息
     */
    private String message;

    /**
     * HTTP code
     */
    private Integer code;

    public ResponseMessage(Object data, String message, String s, int code){
        this.data = data;
        this.message = message;
        this.code = code;
    }


    public ResponseMessage(String message,Integer code){
        this.message = message;
        this.code = code;
    }

    public ResponseMessage() {

    }

    public byte[] toBytes(){

        return JSONObject.toJSONBytes((JSONObject) JSONObject.toJSON(this));
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public JSONArray getDebug() {
        return debug;
    }

    public void setDebug(JSONArray debug) {
        this.debug = debug;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
