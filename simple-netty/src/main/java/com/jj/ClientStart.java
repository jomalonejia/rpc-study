package com.jj;

import com.jj.client.Client;
import com.jj.model.MessageRequest;
import com.jj.service.HelloService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class ClientStart {

    private static AtomicInteger messageId = new AtomicInteger();

    private static <T> T create(Class<T> interfaceClass){
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MessageRequest messageRequest = new MessageRequest();
                        messageRequest.setMessageId(String.valueOf(messageId.getAndIncrement()));
                        messageRequest.setClassName(method.getDeclaringClass().getName());
                        messageRequest.setMethodName(method.getName());
                        messageRequest.setTypeParameters(method.getParameterTypes());
                        messageRequest.setParametersVal(args);
                        Client.sendMessage(messageRequest);
                        return null;
                    }
                });
    }

    public static void main(String[] args){
        HelloService helloService = create(HelloService.class);
        helloService.sayHi("aluba");
    }
}
