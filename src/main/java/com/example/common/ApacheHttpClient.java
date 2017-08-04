package com.example.common;

import com.alibaba.fastjson.JSONObject;
import com.example.utils.HttpUtils;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class ApacheHttpClient {

    public static HttpResponse request(String url, String method, String requestData, Map<String, String> headers, Integer timeout) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest httpUriRequest = RequestBuilder
                .create(method)
                .setUri(url)
                .setEntity(new StringEntity(requestData,"UTF-8"))
                .build();
        // set header;
        headers.forEach((k,v)->httpUriRequest.setHeader(k,v));

        return httpclient.execute(httpUriRequest);
    }

}
