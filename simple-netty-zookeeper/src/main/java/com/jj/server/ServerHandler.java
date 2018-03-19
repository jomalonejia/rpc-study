package com.jj.server;

import com.jj.protocol.model.MessageRequest;
import com.jj.protocol.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class ServerHandler extends SimpleChannelInboundHandler {

    private  Map<String, Class<?>> handlerMap;

    public ServerHandler(Map<String, Class<?>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        // 打印看看是不是我们刚才传过来的那个
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
