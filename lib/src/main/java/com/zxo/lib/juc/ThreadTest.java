package com.zxo.lib.juc;

public class ThreadTest {

  private static Object lock = new Object();


  public static void main(String[] args) throws Exception {

//    Thread thread2 = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        try {
//          System.out.println("th2 start");
//          lock.wait();
//          System.out.println("th2 end");
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      }
//    });
//    System.out.println("main wait");
//    newThread("th1");
//    thread2.start();
//    Thread.sleep(2000);

    CustomThread customThread = new CustomThread("测试线程", new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("测试线程执行完毕");
      }
    });
    customThread.start();
    customThread.testCurrentThread();
  }

  private static void newThread(String name) throws Exception {
    new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (lock){
          try {
            System.out.println("th1 start");
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getName() + " get");
        }
      }
    }, name).start();
  }
}
