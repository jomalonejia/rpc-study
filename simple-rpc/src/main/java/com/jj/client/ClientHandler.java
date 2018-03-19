package com.jj.client;

import com.jj.protocol.MessageRequest;
import com.jj.protocol.MessageResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler{

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private volatile Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
            /*ByteBuf buf = (ByteBuf) msg;
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            System.out.println("Client：" + new String(data).trim());*/
        MessageResponse response = (MessageResponse)msg;
        System.out.println(response.getResult());
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public void sendMessage(MessageRequest request){
        logger.info("++++++++++++++++++"+request.toString());
        channel.writeAndFlush(request);
    }
}
