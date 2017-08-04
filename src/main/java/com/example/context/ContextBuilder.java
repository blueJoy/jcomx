package com.example.context;

import com.example.config.ComxConfigLoader;
import com.example.exceptions.ConfigException;
import com.example.http.ComxURL;
import com.example.http.RequestMessage;
import com.example.http.ResponseMessage;
import com.example.schema.Schema;
import com.example.schema.SchemaLoader;

/**
 * 生成Context
 * Created by baixiangzhu on 2017/7/27.
 */
public class ContextBuilder {

    /**
     * 组装上下文
     * @param request
     * @return
     */
    public static Context build(RequestMessage request) throws ConfigException {

        //初始化操作
        ComxConfigLoader.load();

        String traceId = request.initTraceId();

        ComxURL url = request.getUrl();

        String method = request.getMethod();

        Schema schema = SchemaLoader.load(url.getUri().getPath(), method.toLowerCase());

        return new Context.Builder()
                .request(request)
                .response(new ResponseMessage())
                .traceId(traceId)
                .schema(schema)
                .build();
    }

}
