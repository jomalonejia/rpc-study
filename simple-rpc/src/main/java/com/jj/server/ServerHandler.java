package com.jj.server;

import com.jj.client.ClientInitialTask;
import com.jj.protocol.MessageRequest;
import com.jj.protocol.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class ServerHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private  Map<String, Class<?>> handlerMap;

    public ServerHandler(Map<String, Class<?>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        logger.info(logger.toString());
        System.out.println(request.toString());
        Class<?> aClass = handlerMap.get(request.getClassName());
        if(aClass != null){
            Method method = aClass.getMethod(request.getMethodName(), request.getTypeParameters());
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setResult(method.invoke(aClass.newInstance(), request.getParametersVal()));
            channelHandlerContext.writeAndFlush(messageResponse);
        }

    }
}
