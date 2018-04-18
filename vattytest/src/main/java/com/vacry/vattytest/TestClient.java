package com.vacry.vattytest;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TestClient
{
    final String host;

    final int port;

    public TestClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    private String msg;

    public void put(String msg)
    {
        this.msg = msg;
    }

    public void start() throws Exception
    {
        if(null == msg)
        {
            throw new Exception("msg");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            // 开启服务，开启组，开启客户端管道，确定传送ip与端口，设置回返服务
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ch.pipeline().addLast(new TestChannelHandler(msg));
                    }
                });
            // 保存channel异步操作
            ChannelFuture f = b.connect().sync();
            // 异步关闭ChannelFuture
            f.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully().sync();
        }
    }
}
