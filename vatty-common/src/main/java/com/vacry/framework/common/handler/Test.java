package com.vacry.framework.common.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

@Data
public class Test
{
    private String id;

    public static void main(String[] args) throws Exception
    {
        // 1 第一个线程组 是用于接收Client端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 2 第二个线程组 是用于实际的业务处理操作的
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 3 创建一个辅助类Bootstrap，就是对我们的Server进行一系列的配置
        ServerBootstrap b = new ServerBootstrap();
        // 把俩个工作线程组加入进来
        b.group(bossGroup, workerGroup)
            // 我要指定使用NioServerSocketChannel这种类型的通道
            .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp列队缓冲区
            .option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小
            .option(ChannelOption.SO_RCVBUF, 32 * 1024) // 这是接收缓冲大小
            .option(ChannelOption.SO_KEEPALIVE, true) // 保持连接
            // 一定要使用 childHandler 去绑定具体的 事件处理器
            .childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception
                {
                    sc.pipeline().addLast(new ServerHandler());
                }
            });
        // 绑定指定的端口 进行监听
        ChannelFuture f = b.bind(8765).sync();

        // Thread.sleep(1000000);
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}