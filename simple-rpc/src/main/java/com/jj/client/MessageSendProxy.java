package com.jj.client;

import com.jj.protocol.MessageRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class MessageSendProxy<T> implements InvocationHandler {

    private static AtomicInteger messageId = new AtomicInteger();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageId(String.valueOf(messageId.getAndIncrement()));
        messageRequest.setClassName(method.getDeclaringClass().getName());
        messageRequest.setMethodName(method.getName());
        messageRequest.setTypeParameters(method.getParameterTypes());
        messageRequest.setParametersVal(args);
        System.out.println(messageRequest.toString());
        ClientHandler clientHandler = RpcServerLoader.getInstance().getClientHandler();
        clientHandler.sendMessage(messageRequest);
        return null;
    }
}
