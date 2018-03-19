package com.jj.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.protostuff.Rpc;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class RpcServerLoader {

    private volatile static RpcServerLoader rpcServerLoader;

    private final static int parallel = Runtime.getRuntime().availableProcessors() * 2;
    //netty nio线程池
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);

    private static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16,-1);

    private ClientHandler clientHandler;

    private Lock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();

    private RpcServerLoader(){}

    public static RpcServerLoader getInstance(){
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(){
        threadPoolExecutor.submit(new ClientInitialTask(eventLoopGroup, this));
    }

    public ClientHandler getClientHandler() throws InterruptedException {
        try {
            lock.lock();
            if(clientHandler == null){
                signal.await();
            }
            return clientHandler;
        } finally {
            lock.unlock();
        }
    }

    public void unLoad(){
        clientHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setClientHandler(ClientHandler clientHandler) {
        try {
            lock.lock();
            this.clientHandler = clientHandler;
            signal.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
