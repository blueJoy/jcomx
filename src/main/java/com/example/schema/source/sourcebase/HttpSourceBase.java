package com.example.schema.source.sourcebase;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.http.RequestMessage;
import com.example.schema.source.sourcebase.AbstractSourceBase;

import java.io.IOException;
import java.util.HashMap;

/**
 * 处理HTTP资源的类
 * Created by baixiangzhu on 2017/7/28.
 */
public class HttpSourceBase extends AbstractRequestSourceBase {

    private static final String FIELD_URL_PREFIX = "urlPrefix";

    public HttpSourceBase(Config config) {
        super(config);
    }

    @Override
    public Object doRequest(RequestMessage request, Context context) throws IOException {

        return request.execute(context).getData();
    }

    @Override
    public String getUrlPrefix(Context context) throws ConfigException {
        return config.str(FIELD_URL_PREFIX);
    }
}
