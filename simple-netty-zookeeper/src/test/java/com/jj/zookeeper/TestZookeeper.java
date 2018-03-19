package com.jj.zookeeper;


import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.util.List;

/**
 * Created by jomalone_jia on 2018/3/16.
 */
public class TestZookeeper {


    @Test
    public void test1() {
        ZkClient zkClient = new ZkClient(new ZkConnection("192.168.138.129:2181"), 3000);

        //1.create和delete方法
        /*zkClient.createEphemeral("/temp"); //创建临时节点，会话失效后删除
        zkClient.createPersistent("/super/c1", true); //创建持久化节点，true表示如果父节点不存在则创建父节点
        Thread.sleep(10000);
        zkClient.delete("/temp"); //删除节点
        zkClient.deleteRecursive("/super"); //递归删除，如果该节点下有子节点，会把子节点也删除
        */

        //2.设置path和data，并读取子节点和每个节点的内容
        /*zkClient.createPersistent("/super", "1234"); //创建并设置节点的值
        zkClient.createPersistent("/super/c1", "内容一");
        zkClient.createPersistent("/super/c2", "内容二");
        List<String> children = zkClient.getChildren("/super");
        for(String child : children) {
            System.out.print(child + "：");
            String childPath = "/super/" + child;
            String data = zkClient.readData(childPath); //读取指定节点的值
            System.out.println(data);
        }*/

        //3.更新和判断节点是否存在
        zkClient.writeData("/super/c1", "新内容"); //修改指定节点的值
        String cData = zkClient.readData("/super/c1");
        System.out.println(cData);
        System.out.println(zkClient.exists("/super/c1")); //判断指定节点是否存在
        zkClient.deleteRecursive("/super");
        zkClient.close();
    }

    @Test
    public void test2() throws InterruptedException {
        ZkClient zkClient = new ZkClient(new ZkConnection("192.168.138.129:2181", 3000));
        zkClient.subscribeChildChanges("/super", (parentPath, currentChilds) -> {
            System.out.println("parentPath：" + parentPath);
            System.out.println("currentChilds：" + currentChilds);
        });

        Thread.sleep(3000);
        zkClient.createPersistent("/super");
        Thread.sleep(1000);
        zkClient.createPersistent("/super/c1", "内容一");
        Thread.sleep(1000);
        zkClient.createPersistent("/super/c2", "内容二");
        Thread.sleep(1000);
        zkClient.delete("/super/c2");
        Thread.sleep(1000);
        zkClient.deleteRecursive("/super");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test3() throws InterruptedException {
        ZkClient zkClient = new ZkClient(new ZkConnection("192.168.138.129:2181", 3000));
        zkClient.subscribeDataChanges("/super", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("变更节点为：" + s + "，变更数据为：" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("删除的节点为：" + s);
            }
        });
        zkClient.subscribeChildChanges("/super", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("变更子节点为：" + s + "，变更数据为：" + list.toString());
            }
        });
        zkClient.createPersistent("/super", "123");
        Thread.sleep(3000);
        zkClient.writeData("/super", "456", -1);
        Thread.sleep(1000);
        zkClient.createPersistent("/super/c1", "789"); //不会被监控到
        zkClient.deleteRecursive("/super");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
