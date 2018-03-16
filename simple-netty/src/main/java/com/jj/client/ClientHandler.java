package com.jj.client;

import com.jj.model.MessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class ClientHandler extends SimpleChannelInboundHandler{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
            /*ByteBuf buf = (ByteBuf) msg;
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            System.out.println("Client：" + new String(data).trim());*/
        MessageResponse response = (MessageResponse)msg;
        System.out.println(response.getResult());
    }
}
