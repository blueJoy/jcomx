package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.UnknownDecorTypeException;
import com.example.http.ComxURL;
import com.example.schema.DecorFactory;
import com.example.schema.Schema;
import com.example.schema.SchemaLoader;
import com.example.schema.datadecor.AbstractDecor;

/**
 * Created by baixiangzhu on 2017/7/27.
 */
public class ComxHandler {

    /**
     * 处理流程
     * @param context
     */
    public void handle(Context context) {

        try {

            AbstractDecor decor = DecorFactory.create(context.getSchema().getConfig(),AbstractDecor.TYPE_ROOT);

            JSONObject data = new JSONObject();
            decor.decorate(data,context);

            context.getResponse().setData(data);

        } catch (UnknownDecorTypeException e) {
            e.printStackTrace();
            context.getResponse().setMessage(e.getMessage());
        }

    }
}
