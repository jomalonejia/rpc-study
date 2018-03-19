package com.jj.server;

import com.jj.zookeeper.Constant;
import com.jj.zookeeper.ZkClientUtil;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ServerRegistry {

    private String address;

    public ServerRegistry(String address) {
        this.address = address;
    }

    public void registry(String serverAddress){
        ZkClient zkClient = ZkClientUtil.getInstance().getZkClient(address);
        zkClient.createPersistent(Constant.SERVER_PATH+"/"+serverAddress,true);
    }
}
