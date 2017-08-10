package com.example;

import com.example.constant.Constants;
import com.example.handler.NettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 * Created by baixiangzhu on 2017/7/26.
 */
@Slf4j
public class BootStrap {

    private static final int DEFAULT_PORT = 8888;

    public static void main(String[] args) throws InterruptedException {

        int port = 0;

        if(args != null && args.length > 0){
            port = Integer.parseInt(args[0]);
            log.info("args port = [{}]",port);
        }

        String envPort = System.getProperty(Constants.ENV_PORT);

        if(envPort != null && envPort.isEmpty()){
            port = Integer.parseInt(envPort);
            log.info("env port = [{}]",port);
        }

        if (port == 0){
            port = DEFAULT_PORT;
        }

        start(port);
    }

    public static void start(int port) throws InterruptedException {

        long starTime = System.currentTimeMillis();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())   //netty自带的http解码器
                                    .addLast(new HttpObjectAggregator(65536))  //用于处理post/put请求的body
                                    .addLast(new NettyHandler());
                        }
                    });

            Channel channel = bootstrap.bind(DEFAULT_PORT).sync().channel();

            long endTime = System.currentTimeMillis();
            long time = endTime - starTime;
            log.info("\nStart Time: " + time / 1000 + " s");
            log.info("...............................................................");
            log.info("..................Server starts successfully...................");
            log.info("...............................................................");

            //监听关闭
            channel.closeFuture().sync();

        }finally {
            //释放线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
