package com.zxo.lib.pattern.Proxy;

import java.lang.ref.WeakReference;

public class RunnableProxy implements Runnable {
  WeakReference<ProxyTest> leakRef;
  public RunnableProxy(ProxyTest proxy){
    leakRef = new WeakReference<>(proxy);
  }

  @Override
  public void run() {
    int[] array = new int[1024*1024];
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
  }
}
