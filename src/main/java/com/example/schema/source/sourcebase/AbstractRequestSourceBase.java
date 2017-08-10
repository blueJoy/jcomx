package com.example.schema.source.sourcebase;

import com.example.config.Config;
import com.example.context.Context;
import com.example.exceptions.ConfigException;
import com.example.exceptions.SourceException;
import com.example.exceptions.UrlException;
import com.example.http.ComxURL;
import com.example.http.RequestMessage;
import com.example.schema.source.Source;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baixiangzhu on 2017/7/28.
 */
@Slf4j
public abstract class AbstractRequestSourceBase extends AbstractSourceBase {

    public AbstractRequestSourceBase(Config config) {
        super(config);
    }

    @Override
    public Object executeLoading(Context context, Config sourceOptions, HashMap<String, Object> reservedVariables) throws SourceException, ConfigException, IOException {

        log.info("AbstractRequestSourceBase executeLoading start...  reservedVariables=[{}]",reservedVariables);

        String method = sourceOptions.str(Source.FIELD_ON_METHOD, RequestMessage.METHOD_GET);
        int timeout = sourceOptions.intValue(Source.FIELD_TIMEOUT, RequestMessage.DEFAULT_TIMEOUT);

        //先不做参数header过滤限制
        String targetUrl = this.getUrlPrefix(context) + reservedVariables.get(Source.RESERVED_RENDERED_URI);

        try {
            ComxURL url = new ComxURL(targetUrl);

            RequestMessage request = new RequestMessage(url,method,context.getRequest().getData(),
                    context.getRequest().getHeaders(),timeout);

            return this.doRequest(request,context);

        } catch (UrlException e) {
            throw new SourceException(e);
        }
    }


    public abstract Object doRequest(RequestMessage request, Context context) throws IOException;
    public abstract String getUrlPrefix(Context context) throws ConfigException;

}
