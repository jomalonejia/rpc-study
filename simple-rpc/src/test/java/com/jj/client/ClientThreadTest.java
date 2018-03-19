package com.jj.client;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ClientThreadTest {
    @Test
    public void test1() throws InterruptedException {
        final MessageSendExecutor executor = new MessageSendExecutor();
        ClientThread client = new ClientThread(executor);
        new Thread(client).start();
        //Thread.sleep(1000 * 60);
        //并行度10000
        /*int parallel = 1;

        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);

        for (int index = 0; index < parallel; index++) {
            ClientThread client = new ClientThread(executor, signal, finish, index);
            new Thread(client).start();
        }

        //10000个并发线程瞬间发起请求操作
        signal.countDown();
        finish.await();

        sw.stop();

        String tip = String.format("RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);

        executor.stop();*/
    }
}
