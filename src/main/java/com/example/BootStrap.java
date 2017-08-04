package com.example;

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

/**
 * 启动类
 * Created by baixiangzhu on 2017/7/26.
 */
public class BootStrap {

    private static final int PORT = 8888;

    public static void main(String[] args) throws InterruptedException {

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

            Channel channel = bootstrap.bind(PORT).sync().channel();

            long endTime = System.currentTimeMillis();
            long time = endTime - starTime;
            System.out.println("\nStart Time: " + time / 1000 + " s");
            System.out.println("...............................................................");
            System.out.println("..................Service starts successfully..................");
            System.out.println("...............................................................");


            //监听关闭
            channel.closeFuture().sync();

        }finally {
            //释放线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
