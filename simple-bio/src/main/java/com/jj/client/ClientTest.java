package com.jj.client;

import com.jj.server.ServerCenter;
import com.jj.service.HelloService;
import com.jj.service.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerCenter serverCenter = new ServerCenter(8080);
                serverCenter.registry(HelloService.class, HelloServiceImpl.class);
                try {
                    serverCenter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        signal.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HelloService helloService = RpcClient.getProxy(HelloService.class, new InetSocketAddress(8080));
                    System.out.println(helloService.sayHi("xiaoming"));
                    finish.countDown();
                }
            }).start();
        }

        signal.countDown();
        finish.await();

    }
}
