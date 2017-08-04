package com.example.http;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by baixiangzhu on 2017/8/2.
 */
public class ResponseMessageTest {

    @Test
    public void testToBytes(){

        ResponseMessage responseMessage = new ResponseMessage();
        Map<String,Object> map = Maps.newHashMap();
        map.put("xxx",11);
        map.put("yyy","aa");
        map.put("zzz","kajsdlf");
        responseMessage.setData(map);
        responseMessage.setCode(200);

        Assert.assertEquals("{\"code\":200,\"data\":{\"yyy\":\"aa\",\"xxx\":11,\"zzz\":\"kajsdlf\"}}",new String(responseMessage.toBytes()));

    }

}
