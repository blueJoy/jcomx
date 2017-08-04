package com.example.common;

import com.example.context.Context;
import com.example.context.ContextBuilder;
import com.example.handler.ComxHandler;
import com.example.http.RequestMessage;
import com.example.http.ResponseMessage;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
public class Exectors {

    public static ResponseMessage execute(RequestMessage request){

        Context context;

        ResponseMessage response;

        try {
            context = ContextBuilder.build(request);

            ComxHandler handler = new ComxHandler();
            handler.handle(context);

            response = context.getResponse();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(e.getMessage(),500);
        }

    }
}
