package com.vacry.vatty;

import org.apache.log4j.Logger;

import com.vacry.vatty.annotation.reflect.ActionReflect;
import com.vacry.vatty.annotation.reflect.AutowiredReflect;
import com.vacry.vatty.util.StringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter
{
    private final Logger log = Logger.getLogger(getClass());

    private FullHttpRequest request;

    private static ActionReflect action;

    static
    {
        action = ActionReflect.getInstance();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        // 初始化
        AutowiredReflect.inject(this);
        // 前置处理
        request = (FullHttpRequest)msg;
        String uri = request.uri();
        ByteBuf buf = request.content();
        String str = buf.toString(CharsetUtil.UTF_8);
        buf.release();

        // 路由转发，@interface定义路径
        String result = action.requestDispatch(uri, str);

        // 后置处理
        FullHttpResponse response;
        if(StringUtil.isEmpty(result))
        {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
                Unpooled.wrappedBuffer("".getBytes("UTF-8")));
        }
        else
        {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(result.getBytes("UTF-8")));
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if(HttpUtil.isKeepAlive(request))
        {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        log.error(cause.getMessage());
        ctx.close();
    }
}