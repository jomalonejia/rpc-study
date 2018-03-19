package com.jj.client;

import com.jj.client.AbortPolicyWithReport;
import com.jj.client.CustomThreadFactory;

import java.util.concurrent.*;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class RpcThreadPool {
    public static Executor getExecutor(int threads, int queues) {
        String name = "RpcThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<>()
                        : (queues < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queues)),
                new CustomThreadFactory(name, false), new AbortPolicyWithReport(name));
    }
}
