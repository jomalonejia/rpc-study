package com.jj.client;

import com.jj.service.HelloService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ClientThread implements Runnable {
    private CountDownLatch signal;
    private CountDownLatch finish;
    private MessageSendExecutor executor;
    private int taskNumber = 0;

    public ClientThread(MessageSendExecutor executor, CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.executor = executor;
    }

    public ClientThread(MessageSendExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            signal.await();
            HelloService helloService = executor.create(HelloService.class);
            helloService.sayHi("aluba");
            finish.countDown();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
