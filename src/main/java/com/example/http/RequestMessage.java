package com.example.http;

import com.alibaba.fastjson.JSONObject;
import com.example.common.ApacheHttpClient;
import com.example.common.ArrayAccessBase;
import com.example.constant.Constants;
import com.example.context.Context;
import com.example.context.ContextBuilder;
import com.example.handler.ComxHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by baixiangzhu on 2017/7/26.
 */
public class RequestMessage implements ArrayAccessBase{

    public static final String METHOD_GET      = "get";
    public static final String METHOD_POST     = "post";
    public static final String METHOD_DELETE   = "delete";
    public static final String METHOD_PUT      = "put";
    public static final Integer DEFAULT_TIMEOUT     = 10;

    private static final String FIELD_URL = "url";
    private static final String FIELD_METHOD = "method";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_HEADER = "headers";


    //请求url
    protected ComxURL url;
    //HTTP 请求方法 GET/POST/PUT/DELETE
    protected String method;
    //存放数据
    protected Map<String,Object> data;
    //请求头
    protected Map<String,String> headers;
    //请求超时时间
    protected Integer timeout;


    public RequestMessage(ComxURL url,String method,Map<String,Object> data,Map<String,String> headers,Integer timeout){
        this.url = url;
        this.method = method;
        this.data = data;
        this.headers = headers;
        this.timeout = timeout;
    }



    public String getMethod() {
        return method;
    }

    public ComxURL getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 执行真正的逻辑
     * @return
     * @param context
     */
    public ResponseMessage execute(Context context) throws IOException {

        String targetUrl = this.url.getUrl();

        String requestData = data != null ? new JSONObject(data).toJSONString() : "";

        try {
            HttpResponse response = ApacheHttpClient.request(targetUrl, method.toUpperCase(), requestData, headers, timeout);

            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                String responseBody = entity != null ? EntityUtils.toString(entity) : null;
                JSONObject responseJson = JSONObject.parseObject(responseBody);

                return new ResponseMessage(responseJson.get("data"),responseJson.getString("message"),toString(),statusCode);
            }else{

                throw new ClientProtocolException("Unexpected response status: " + statusCode);

            }

        } catch (IOException e) {
            throw e;
        }

    }

    /**
     * 初始化traceId
     * @return
     */
    public String initTraceId(){

        String traceId = this.getTraceId();
        if(!traceId.isEmpty()) return traceId;

        return generatorTraceId();
    }

    /**
     * 生成traceId
     * @return
     */
    private String generatorTraceId(){

        String traceId = Constants.DEFAULT_TRACE_ID_PREFIX
                + UUID.randomUUID().toString().replace("-","").toUpperCase();

        //放入header中
        this.headers.put(Constants.HEADER_FIELD_TRACE_ID,traceId);

        return traceId;
    }


    /**
     * traceId 追踪标志
     * traceId 是用于追踪日志用的标识字符串, 通过header X-Gomeplus-Trace-Id或 url query traceId传递, header 和URL并存时,以URL为准.
     * @return
     */
    public String getTraceId(){

        //从请求参数中获取traceId
        String traceId = this.url.getUrlQuery().getParameterValue(Constants.QUERY_FIELD_TRACE_ID);

        if(null != traceId) return traceId;

        //从header中获取traceId
        traceId = this.headers.get(Constants.HEADER_FIELD_TRACE_ID);

        return traceId != null ? traceId : "";
    }



    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) {

        switch (key.toString().toLowerCase()){
            case FIELD_URL : return url;
            case FIELD_DATA: return data;
            case FIELD_HEADER: return headers;
            case FIELD_METHOD: return method;
            default: return null;
        }
    }
}
