package com.zxo.lib.base;

public class ClinitTest {
  public static void main(String[] args) {
    Runnable runnable = () ->{
      System.out.println(Thread.currentThread().getName() + "start..");
      InitThread initThread = new InitThread();
      System.out.println(Thread.currentThread().getName() + "end..");
    };

    Thread t1 = new Thread(runnable, "thread-1");
    Thread t2 = new Thread(runnable, "thread-2");
    t1.start();
    t2.start();
  }

  static class InitThread {

    static {
      if (true){
        System.out.println(Thread.currentThread().getName() + " 正在加载..");
        while (true){

        }
      }
    }

  }
}
