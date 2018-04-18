package com.vacry.vattytest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable
public class TestChannelHandler extends SimpleChannelInboundHandler<ByteBuf>
{
    private String msg;

    TestChannelHandler(String msg)
    {
        this.msg = msg;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException
    {
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception
    {
        System.out.println("回传信息为：" + msg.toString(CharsetUtil.UTF_8));
    }
}
