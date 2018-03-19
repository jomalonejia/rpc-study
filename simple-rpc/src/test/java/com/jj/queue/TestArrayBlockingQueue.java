package com.jj.queue;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class TestArrayBlockingQueue {
    class Apple {
        public Apple(){
        }
    }

    /**
     * 生产者线程
     */
    class Producer implements Runnable{
        private final ArrayBlockingQueue<Apple> mAbq;
        Producer(ArrayBlockingQueue<Apple> arrayBlockingQueue){
            this.mAbq = arrayBlockingQueue;
        }

        @Override
        public void run() {
            while (true) {
                Produce();
            }
        }

        private void Produce(){
            try {
                Apple apple = new Apple();
                mAbq.put(apple);
                System.out.println("生产:"+apple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消费者线程
     */
    class Consumer implements Runnable{

        private ArrayBlockingQueue<Apple> mAbq;
        Consumer(ArrayBlockingQueue<Apple> arrayBlockingQueue){
            this.mAbq = arrayBlockingQueue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    comsume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void comsume() throws InterruptedException {
            Apple apple = mAbq.take();
            System.out.println("消费Apple="+apple);
        }
    }

    @Test
    public void test1() throws InterruptedException {
       ArrayBlockingQueue<Apple> queue= new ArrayBlockingQueue<>(1);
        Thread thread1 = new Thread(new Producer(queue));
        Thread thread2 = new Thread(new Producer(queue));
        Thread thread3 = new Thread(new Consumer(queue));
        Thread thread4 = new Thread(new Consumer(queue));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }
}
