package com.zxo.lib.juc;

public class CustomThread extends Thread{

  private Runnable runnable;
  public CustomThread(String name, Runnable runnable){
    super(runnable, name);
  }

  public void testCurrentThread(){
    System.out.println("当前线程="+Thread.currentThread().getName());
  }
}
