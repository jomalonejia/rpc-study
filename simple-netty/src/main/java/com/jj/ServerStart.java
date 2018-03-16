package com.jj;

import com.jj.server.Server;
import com.jj.service.HelloService;
import com.jj.service.HelloServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class ServerStart {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Class<?>> handlerMap = new HashMap<>();
        handlerMap.put(HelloService.class.getName(), HelloServiceImpl.class);
        Server server = new Server(8080,handlerMap);
        server.start();
    }
}
