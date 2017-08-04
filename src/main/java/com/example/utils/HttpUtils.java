package com.example.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * HTTP的工具包
 *      依赖了fastjson、http client、slf4j包
 *      JDK版本1.8+
 *
 * 主要功能：GET、POST、PUT、DELETE请求
 *
 * Created by baixiangzhu on 2017/7/24.
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(5000).build();


    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String URL_IS_NULL_EXCEPTION = "the http url is null";
    private static final String DEFAULT_CONTENT_TYPE_KEY="content-type";
    private static final String DEFAULT_CONTENT_TYPE_VALUE="application/json;charset=utf-8";
    /**
     * 默认为application/json..如果有其他Content-Type的需求，需进行修改
     */
    private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;


    private static final String HTTP = "http:";
    private static final String HTTPS = "https:";


    /**
     * 获取连接
     *      默认使用：PoolingHttpClientConnectionManager 连接池管理，默认最大20个连接
     * @return
     */
    private static CloseableHttpClient getHttpClient(String url) {

        if(url.contains(HTTPS))
            return HttpClients.custom().setSSLSocketFactory(createSSLSocketFacotry()).build();

        return HttpClients.createDefault();
    }

    private static LayeredConnectionSocketFactory createSSLSocketFacotry() {

        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();

            return new SSLConnectionSocketFactory(sslContext);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**              ==========================      GET 请求   =======================     **/

    /**
     * 参数已经在url拼装好的含请求头设置的get请求
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url,Map<String,String> headers) {
        isNotNull(url);
        return getResult(new HttpGet(url),headers);
    }

    /**
     * 参数已经在url拼装好的无请求头设置的get请求
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url,null);
    }



    /**
     * 根据url和queryParameters 组装get请求，获取返回值，含请求头
     * @param url  url
     * @param headers  请求头
     * @param queryParameters 请求参数
     * @return
     */
    public static String getFromQueryParameter(String url,Map<String,String> headers,Map<String,String> queryParameters){

        isNotNull(url);

        if(queryParameters == null || queryParameters.isEmpty()) return get(url);

        //根据url获取URI
        URI uri = getFromUrlAndParameters(url, queryParameters);

        if(uri == null) return null;

        return getResult(new HttpGet(uri),headers);

    }

    /**
     * 根据url和queryParameters 组装get请求，获取返回值，无请求头设置
     * @param url
     * @param queryParameters
     * @return
     */
    public static String getFromQueryParameter(String url,Map<String,String> queryParameters){

        return getFromQueryParameter(url,null,queryParameters);

    }



    /**              ==========================      POST 请求   =======================     **/

    /**
     *无header的POST请求
     * @param url
     * @param body
     * @return
     */
    public static String post(String url,Map<String,Object> body){

        return post(url,body,null);
    }

    /**
     * 有header的POST请求
     * @param url
     * @param body
     * @param headers
     * @return
     */
    public static String post(String url,Map<String,Object> body,Map<String,String> headers){

        isNotNull(url);

        HttpPost post = new HttpPost(url);

        //setEntity(post,body,headers);
        post.setEntity(new StringEntity(JSON.toJSONString(body), DEFAULT_CONTENT_TYPE));

        return getResult(post,headers);
    }


    /**
     * url含参数的无header的POST请求
     * @param url
     * @param body
     * @param queryParameters
     * @return
     */
    public static String postFromQueryParameters(String url,Map<String,Object> body,Map<String,String> queryParameters){

        return postFromQueryParameters(url,body,null,queryParameters);
    }

    /**
     * url含参数的含header的POST请求
     * @param url
     * @param body
     * @param headers
     * @param queryParameters
     * @return
     */
    public static String postFromQueryParameters(String url,Map<String,Object> body,Map<String,String> headers,Map<String,String> queryParameters){

        isNotNull(url);

        if(queryParameters == null || queryParameters.isEmpty()) return post(url,body);

        //根据url获取URI
        URI uri = getFromUrlAndParameters(url, queryParameters);

        if(uri == null) return null;

        HttpPost post = new HttpPost(uri);

        if(body != null && !body.isEmpty()){
            post.setEntity(new StringEntity(JSON.toJSONString(body),DEFAULT_CONTENT_TYPE));
        }

        return getResult(post,headers);
    }

    /**
     * Post 上传文件
     * @param url
     * @param file
     * @return
     */
    public static String postFile(String url,File file){
        return postFile(url,file,null,null);
    }

    /**
     * url含参数Post上传文件
     * @param url
     * @param file
     * @param queryParameters
     * @return
     */
    public static String postFile(String url,File file,Map<String,String> queryParameters){

        return postFile(url,file,queryParameters,null);
    }

    /**
     * 含header的Post上传文件
     * @param url
     * @param file
     * @param queryParameters
     * @param headers
     * @return
     */
    public static String postFile(String url,File file,Map<String,String> queryParameters,Map<String,String> headers){

        HttpPost post;

        if(queryParameters == null || queryParameters.isEmpty()){

            post = new HttpPost(url);

        }else{
            URI uri = getFromUrlAndParameters(url, queryParameters);
            if(uri == null) return null;
            post = new HttpPost(uri);
        }

/*        post.setEntity( MultipartEntityBuilder.create()
                .addBinaryBody("file",file).build());*/

        return getResult(post,headers);

    }





    /**              ==========================      PUT 请求   =======================     **/

    /**
     *无header的PUT请求
     * @param url
     * @param body
     * @return
     */
    public static String put(String url,Map<String,Object> body){

        return put(url,body,null);
    }

    /**
     * 有header的PUT请求
     * @param url
     * @param body
     * @param headers
     * @return
     */
    public static String put(String url,Map<String,Object> body,Map<String,String> headers){

        isNotNull(url);

        HttpPut put = new HttpPut(url);

        if(body != null && !body.isEmpty()){
            put.setEntity(new StringEntity(JSON.toJSONString(body),DEFAULT_CONTENT_TYPE));
        }

        return getResult(put,headers);
    }

    /**
     * url含参数的无header的PUT请求
     * @param url
     * @param body
     * @param queryParameters
     * @return
     */
    public static String putFromQueryParameters(String url,Map<String,Object> body,Map<String,String> queryParameters){

        return putFromQueryParameters(url,body,null,queryParameters);
    }

    /**
     * url含参数的含header的PUT请求
     * @param url
     * @param body
     * @param headers
     * @param queryParameters
     * @return
     */
    public static String putFromQueryParameters(String url,Map<String,Object> body,Map<String,String> headers,Map<String,String> queryParameters){

        isNotNull(url);

        if(queryParameters == null || queryParameters.isEmpty()) return put(url,body);

        //根据url获取URI
        URI uri = getFromUrlAndParameters(url, queryParameters);

        if(uri == null) return null;

        HttpPut put = new HttpPut(uri);

        if(body != null && !body.isEmpty()){
            put.setEntity(new StringEntity(JSON.toJSONString(body),DEFAULT_CONTENT_TYPE));
        }

        return getResult(put,headers);
    }



    /**              ==========================      DELETE 请求   =======================     **/

    /**
     *无header的PUT请求
     * @param url
     * @return
     */
    public static String delete(String url){

        return delete(url,null);
    }

    /**
     * 有header的PUT请求
     * @param url
     * @param headers
     * @return
     */
    public static String delete(String url,Map<String,String> headers){

        isNotNull(url);

        HttpDelete httpDelete = new HttpDelete(url);

        return getResult(httpDelete,headers);
    }

    /**
     * url含参数的无header的PUT请求
     * @param url
     * @param queryParameters
     * @return
     */
    public static String deleteFromQueryParameters(String url,Map<String,String> queryParameters){

        return deleteFromQueryParameters(url,null,queryParameters);
    }

    /**
     * url含参数的含header的PUT请求
     * @param url
     * @param headers
     * @param queryParameters
     * @return
     */
    public static String deleteFromQueryParameters(String url,Map<String,String> headers,Map<String,String> queryParameters){

        isNotNull(url);

        if(queryParameters == null || queryParameters.isEmpty()) return delete(url,headers);

        //根据url获取URI
        URI uri = getFromUrlAndParameters(url, queryParameters);

        if(uri == null) return null;

        HttpDelete httpDelete = new HttpDelete(uri);


        return getResult(httpDelete,headers);
    }



    /**
     * http 设置body
     * @param body
     */
    private static void setEntity(HttpEntityEnclosingRequestBase request, Map<String, Object> body, Map<String,String> headers) {

        if(body != null && !body.isEmpty()){

            String contentType = null;

            if(headers == null || headers.isEmpty() || !isContainsContentType(headers,contentType)){

                request.setEntity(new StringEntity(JSON.toJSONString(body), DEFAULT_CONTENT_TYPE));
                return;
            }

            StringEntity entity = new StringEntity(JSON.toJSONString(body), DEFAULT_CHARSET);
            entity.setContentType(contentType);
            request.setEntity(entity);
        }

    }


    /**
     * 是否包含ContentType 设置
     * @param headers
     * @param contentType
     * @return
     */
    private static boolean isContainsContentType(Map<String, String> headers, String contentType) {

        for (String key : headers.keySet()){
            if(key != null && DEFAULT_CONTENT_TYPE_KEY.equals(key.toLowerCase())){
                contentType = headers.get(key);
                return true;
            }
        }
        return false;
    }




    private static URI getFromUrlAndParameters(String url,Map<String,String> queryParameters){

        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);

            uriBuilder.setParameters(convert2Nvps(queryParameters));

            return uriBuilder.build();

        } catch (URISyntaxException e) {
           log.error("getFromUrlAndParameters parse url error.url=[{}]",url,e);
        }
        return null;
    }

    public static void main(String[] args) {

        String url = "http://www.baidu.com";
        Map<String,String> paramters = new HashMap<>();
        paramters.put("id","1111");

        URI uri = getFromUrlAndParameters(url, paramters);

        System.out.println(uri);

    }


    private static String getResult(HttpRequestBase request, Map<String,String> headers){

        String resultContent = null;

        CloseableHttpClient client = getHttpClient(request.getURI().toString());
        try {

            //设置配置
            request.setConfig(config);

            //设置请求头
            setHeaders(request,headers);

            CloseableHttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();

            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){

                log.error("the http get response statusCode is not 200!url=[{}]",request.getURI());

            }else {
                resultContent =  EntityUtils.toString(entity,DEFAULT_CHARSET);
            }

            //确保流已关闭
            EntityUtils.consume(response.getEntity());


        } catch (IOException e) {
            log.error("the get http is error! url = [{}]",request.getURI(),e);
        }

        return resultContent;

    }




    private static List<NameValuePair> convert2Nvps(Map<String, String> queryParameters) {

        List<NameValuePair> nvps = new ArrayList<>();

        queryParameters.forEach((k,v) ->nvps.add(new BasicNameValuePair(k,v)));

        return nvps;
    }


    private static void setHeaders(HttpRequestBase requestBase, Map<String, String> headers) {
        if(requestBase == null || headers == null) return;
        headers.entrySet().forEach( e -> requestBase.setHeader(e.getKey(),e.getValue()));
    }

    public static void isNotNull(Object object) {

        if(object == null) {
            throw new IllegalArgumentException(URL_IS_NULL_EXCEPTION);
        }
    }

}
