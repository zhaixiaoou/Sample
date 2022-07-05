package com.zxo.lib.juc;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产消费者模式
 */
public class Prosumer {

  private final LinkedList<Product> data = new LinkedList<>();
  private int maxSize = 20;

  private final Object lock = new Object();

  public void put(){
    synchronized (lock){
      while (data.size() == maxSize){
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      data.add(new Product());
      System.out.println("仓库里有了" + data.size() + "个产品。");
      lock.notify();
    }
  }

  public void take(){
    synchronized (lock){
      while (data.size() == 0){
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      System.out.println("拿到了" + data.poll() + "，现在仓库还剩下" + data.size());
      lock.notify();
    }
  }


  class Product{

  }
}
