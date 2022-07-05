package com.zxo.lib.juc;

public class Customer implements Runnable{

  private Prosumer prosumer;

  public Customer(Prosumer prosumer){
    this.prosumer = prosumer;
  }

  @Override
  public void run() {
    for (int i = 0; i < 100; i++) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      prosumer.take();
    }
  }
}
