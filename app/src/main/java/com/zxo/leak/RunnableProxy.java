package com.zxo.leak;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class RunnableProxy implements Runnable {
  private WeakReference<Activity> ref;
  public RunnableProxy(Activity host){
    ref = new WeakReference<>(host);
  }

  @Override
  public void run() {
    int[] array = new int[1024*1024];
  }
}
