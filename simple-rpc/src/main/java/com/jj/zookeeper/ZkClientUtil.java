package com.jj.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ZkClientUtil {
    private volatile static ZkClientUtil zkClientUtil;

    private final static int DEFAULT_SESSION_TIMEOUT= 5000;


    private ZkClientUtil(){}

    public static ZkClientUtil getInstance(){
        if(zkClientUtil == null){
            synchronized (ZkClientUtil.class) {
                if(zkClientUtil == null){
                    zkClientUtil = new ZkClientUtil();
                }
            }
        }
        return zkClientUtil;
    }

    public ZkClient getZkClient(String address){
        ZkClient zkClient = new ZkClient(address, DEFAULT_SESSION_TIMEOUT);
        if(zkClient == null) throw new RuntimeException("no zkClient exception");
        return zkClient;
    }

}
