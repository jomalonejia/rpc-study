package com.jj.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class MessageSendExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ClientInitialTask.class);

    private RpcServerLoader loader = null;

    public MessageSendExecutor() {
        logger.info("load begin ....");
        loader = RpcServerLoader.getInstance();
        loader.load();
        logger.info("load finished ....");
    }

    public void stop(){
        loader.unLoad();
    }

    public <T> T create(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new MessageSendProxy<T>());
    }
}
