package com.zxo.lib.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {

  private static int sum;

  public static void main(String[] args) {

    testCondition();
//    testSynchronized();
  }

  /**
   * synchronized 实现 线程依次执行
   * ABABABABABAB
   */
  private static void testSynchronized() {
    Object lock = new Object();

    Thread thread1=new Thread(new Runnable() {
      @Override
      public void run() {
        while (sum < 20){
          synchronized (lock){
            sum = sum+1;
            System.out.println("线程1,sum="+sum);
            lock.notifyAll();
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    });

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        while (sum < 20){
          synchronized (lock){
            sum = sum+1;
            System.out.println("线程2,sum="+sum);
            lock.notifyAll();
            try {
              lock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    });

    thread1.start();
    thread2.start();
  }

  /**
   * lock condition 实现 依次执行
   */
  private static void testCondition(){
    Lock lock = new ReentrantLock();
    Condition singleCondition = lock.newCondition();
    Condition twoCondition = lock.newCondition();
    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i=0; i< 100; i++){
          try {
            lock.lock();
            if (sum %2 == 0){
              sum = sum+1;
              System.out.println("线程1,  sum="+sum);
              twoCondition.signal();

            } else {
              if (i < 99) {
                singleCondition.await();
              }

            }
            lock.unlock();

          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }

      }
    });

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i=0; i< 100; i++){
          try {
            lock.lock();
            if (sum %2 != 0){
              sum = sum+1;
              System.out.println("线程2,  sum="+sum);
              singleCondition.signal();

            } else {
              if (i < 99){
                twoCondition.await();
              }

            }
            lock.unlock();

          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    thread1.start();
    thread2.start();
  }

  private static void test(){
    ReentrantLock lock = new ReentrantLock();
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        lock.lock();
        System.out.println("线程1 获取锁资源");
        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        lock.unlock();
      }
    }).start();


    new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
        System.out.println("线程2 获取锁资源");
        try {
          System.out.println("线程2 释放锁资源 中断状态="+Thread.currentThread().isInterrupted());
          Thread.sleep(100L);
          lock.unlock();


          lock.lock();
          System.out.println("线程2 重新获取锁资源");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        lock.unlock();
      }
    }).start();
  }
}
