package com.jj.server;

import com.jj.zookeeper.Constant;
import com.jj.zookeeper.ZkClientUtil;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ServerDiscovery {

    private String address;

    public ServerDiscovery(String address) {
        this.address = address;
    }

    public String discovery(){
        String node = null;
        ZkClient zkClient = ZkClientUtil.getInstance().getZkClient(address);
        List<String> nodes = zkClient.getChildren(Constant.SERVER_PATH);
        int size = nodes.size();
        if(size > 0){
            if(size == 0){
                node = nodes.get(0);
            }else{
                node =  nodes.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        return node;
    }
}
