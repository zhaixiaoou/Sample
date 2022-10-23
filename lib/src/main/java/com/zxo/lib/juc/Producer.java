package com.zxo.lib.juc;

public class Producer implements Runnable{
  private Prosumer prosumer;
  public Producer(Prosumer prosumer){
    this.prosumer = prosumer;
  }

  @Override
  public void run() {
    for (int i = 0; i < 100; i++) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      prosumer.put();
    }
  }
}
