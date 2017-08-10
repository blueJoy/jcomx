package com.example.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.common.Exectors;
import com.example.http.ComxURL;
import com.example.http.RequestMessage;
import com.example.http.ResponseMessage;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by baixiangzhu on 2017/7/26.
 */
@Slf4j
public class NettyHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static final String Content_Length = "Content-Length";

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {

        long statTime = System.currentTimeMillis();

        if(HttpUtil.is100ContinueExpected(request)){
            ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.CONTINUE));
        }

        log.info("the request info = [{}]",request);

        //获取请求资源的路径
        String uri = request.uri();

        //请求方式   GET/POST/PUT/DELETE等
        String method = request.method().name();

        //请求头
        HttpHeaders headers = request.headers();

        HashMap<String,String> headerMaps = Maps.newHashMap();
        headers.entries().forEach(e -> headerMaps.put(e.getKey(),e.getValue()));

        //解析url
        ComxURL url = new ComxURL(uri);

        //读取POST/PUT的requestBody 的数据
        //TODO:暂不支持文件上传
        JSONObject requestData = null;
        if(RequestMessage.METHOD_POST.toUpperCase().equals(method)
                || RequestMessage.METHOD_PUT.toUpperCase().equals(method)){

            requestData = readRequestData(request,method);
        }

        //remove ContentLength
        removeContentLength(headerMaps);
        headerMaps.remove("content-length");

        RequestMessage requestMessage = new RequestMessage(url,method,requestData,headerMaps,0);

        log.info("build requestMessage is successfully!");

        //执行逻辑
        ResponseMessage responseMessage = Exectors.execute(requestMessage);


        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseMessage.toBytes()));

        //           response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().setInt("Content-Length", response.content().readableBytes());


        if(!keepAlive){
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }else{
           response.headers().set("Content-Type","keep-alive");
            ChannelFuture future = ctx.write(response);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println(channelFuture.isSuccess());
                }
            });
        }

        long endTime = System.currentTimeMillis();

        log.info("handle this url = [{}] expend [{}] ms.",url,(endTime - statTime));
    }

    //删除Content-Length  因为后面setEntity会检查
    private void removeContentLength(HashMap<String, String> headerMaps) {
        Iterator<Map.Entry<String, String>> iterator = headerMaps.entrySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().getKey();
            if(Content_Length.equalsIgnoreCase(key)){
                iterator.remove();
                return;
            }
        }
    }

    /**
     * 读取body中的数据
     * @param request
     * @param method
     * @return
     */
    private JSONObject readRequestData(HttpRequest request, String method) {

        if(!!!(request instanceof HttpContent))
            return null;

        HttpContent httpContent = (HttpContent) request;

        ByteBuf content = httpContent.content();

        //缓冲区
        byte[] buf = new byte[content.readableBytes()];
        //读取数据
        content.readBytes(buf);

        return JSONObject.parseObject(new String(buf, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
