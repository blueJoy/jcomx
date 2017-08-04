package com.example.http;

import com.alibaba.fastjson.JSONObject;
import com.example.exceptions.UrlException;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by baixiangzhu on 2017/8/4.
 */
public class RequestMessageTest {

    @Test
    public void testRequestMessage(){

        String url = "http://localhost:8080/user?userId=123";

        try {
            ComxURL comxURL = new ComxURL(url);
            String method = "GET";
            JSONObject data = new JSONObject();
            data.put("username","test");
            data.put("time",new Date().getTime());

            Map<String,String> headers = Maps.newHashMap();
            headers.put("Content-Type","application/json");
            headers.put("traceId", UUID.randomUUID().toString());

            RequestMessage requestMessage = new RequestMessage(comxURL,method,data, (HashMap<String, String>) headers,1000);

            Assert.assertEquals("GET",requestMessage.get("method"));
            Assert.assertEquals(2,requestMessage.getHeaders().size());
            Assert.assertEquals("http://localhost:8080/user?userId=123",comxURL.getUrl());
            Assert.assertTrue(requestMessage.getUrl().get("query") instanceof URLQuery);
            Assert.assertTrue(requestMessage.get("url") instanceof ComxURL);
            Assert.assertEquals("123",((URLQuery)(((ComxURL)requestMessage.get("url")).get("query"))).get("userId"));


        } catch (UrlException e) {
            e.printStackTrace();
        }

    }

}
