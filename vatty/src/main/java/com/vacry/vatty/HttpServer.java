package com.vacry.vatty;

import com.vacry.vatty.annotation.reflect.ComponentReflect;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer
{
    private final Class<?> clazz;

    public HttpServer(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    public void start(int port) throws Exception
    {
        ComponentReflect.init(clazz);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception
                    {
                        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                        // 把多个HttpMessage组装成一个完整的Http请求或者响应
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                        // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                        // 处理大文件传输
                        // ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                        // 自定义通道处理器，用作业务的实现
                        ch.pipeline().addLast(new HttpServerInboundHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}