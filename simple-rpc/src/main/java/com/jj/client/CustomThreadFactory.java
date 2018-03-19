package com.jj.client;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class CustomThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger mThreadNumber = new AtomicInteger(1);

    private String prefix;

    private boolean isDaemon;

    private ThreadGroup threadGroup;

    public CustomThreadFactory(){
        this("rpcservice-threadpool-"+threadNumber.getAndIncrement(), false);
    }

    public CustomThreadFactory(String prefix){
        this(prefix, false);
    }

    public CustomThreadFactory(String prefix, boolean isDaemon) {
        this.prefix = prefix + "-thread-";
        this.isDaemon = isDaemon;
        SecurityManager securityManager = System.getSecurityManager();
        this.threadGroup = securityManager == null ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + mThreadNumber.getAndIncrement();
        Thread thread = new Thread(threadGroup, r, name);
        return thread;
    }
}
