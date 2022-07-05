package com.zxo.lib.juc;

public class NotifyTest {

  private LockObject lock = new LockObject(true);

  class NotifyThread extends Thread{
    public NotifyThread(String name){
      super(name);
    }

    @Override
    public void run() {
      try {
        sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (lock){
        System.out.println(getName() + " notify");
        lock.lock = false;
        lock.notifyAll();
      }
    }
  }

  class WaitThread extends Thread{
    public WaitThread(String name){
      super(name);
    }

    @Override
    public void run() {
      synchronized (lock){
        while (lock.lock) {
          System.out.println(getName() + " begin waiting!");
          long waitTime = System.currentTimeMillis();
          try {
            lock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          waitTime = System.currentTimeMillis() - waitTime;
          System.out.println("wait time :" + waitTime);
        }
        System.out.println(getName() + " end waiting!");
      }
    }
  }


  class LockObject {
    public boolean lock;
    public LockObject(boolean lock){
      this.lock = lock;
    }
  }
  public static void main(String[] args) {
    System.out.println("Main Thread Run!");
    NotifyTest test = new NotifyTest();
    NotifyTest.NotifyThread notifyThread = test.new NotifyThread("notify01");
    NotifyTest.WaitThread waitThread01 = test.new WaitThread("waiter01");
    NotifyTest.WaitThread waitThread02 = test.new WaitThread("waiter02");
    NotifyTest.WaitThread waitThread03 = test.new WaitThread("waiter03");
    notifyThread.start();
    waitThread01.start();
    waitThread02.start();
    waitThread03.start();

  }
}
