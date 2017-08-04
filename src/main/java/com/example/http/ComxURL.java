package com.example.http;

import com.example.common.ArrayAccessBase;
import com.example.constant.Constants;
import com.example.exceptions.UrlException;
import com.google.common.collect.Maps;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * 存放url的信息
 * Created by baixiangzhu on 2017/7/26.
 */
public class ComxURL implements ArrayAccessBase{

    private static final String FILED_QUERY = "query";


    private String url;
    private URI uri;
    //封装请求的参数
    private URLQuery query;


    public ComxURL(String url) throws UrlException {
        this.url = url;

        try {
            this.uri = new URI(url);
            List<NameValuePair> nvps = URLEncodedUtils.parse(uri, Constants.DEFAULT_CHARSET);

            HashMap<String, String> queryParameters = Maps.newHashMap();
            //TODO: JDK 1.8 如不兼容自行修改
            nvps.forEach(e -> queryParameters.put(e.getName(), e.getValue()));

            query = new URLQuery(uri.getQuery(), queryParameters);

        } catch (URISyntaxException e) {
            throw new UrlException(e);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) {

        switch (key.toString().toLowerCase()){
            case FILED_QUERY: return query;
            default: return null;
        }
    }

    public URLQuery getUrlQuery() {
        return query;
    }

    public URI getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }
}
