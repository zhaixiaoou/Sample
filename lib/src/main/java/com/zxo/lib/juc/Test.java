package com.zxo.lib.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class Test {
  private static Object lock = new Object();
  private static int count = 0;
  public static void main(String[] args) {
   Test  test = new Test();
//   test.lockTest();

    test.synTest();
  }

  public void synTest(){


    new Thread(new Runnable() {
      @Override
      public void run() {
        while(count < 6) {
          synchronized (lock){
            count = count+1;
            System.out.println("线程1--"+ count);
            lock.notifyAll();
            if (count < 6){
              try {
                lock.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    }).start();


    new Thread(new Runnable() {
      @Override
      public void run() {
        while(count < 6) {
          synchronized (lock){
            count = count+1;
            System.out.println("线程2--"+count);
            lock.notifyAll();
            if (count < 6){
              try {
                lock.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }

          }
        }
      }
    }).start();

  }


  public void  lockTest(){
    ReentrantLock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();

    new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
        try {
          for (int i = 0; i< 6; i++){
            if (i%2 == 0) {
              System.out.println("线程1--"+i);
              condition1.await();
            }  else {
              condition2.signal();
            }
          }
        } catch (Exception e){
          e.printStackTrace();
        } finally {
          lock.unlock();
        }

      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();

        try {
          for (int i = 0; i< 6; i++){
            if (i%2 != 0) {
              System.out.println("线程2--"+i);
              condition2.await();
            }  else {
              condition1.signal();
            }
          }
        } catch (Exception e){
          e.printStackTrace();
        } finally {
          lock.unlock();
        }

      }
    }).start();
  }
}
