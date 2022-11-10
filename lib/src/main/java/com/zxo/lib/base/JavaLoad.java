package com.zxo.lib.base;

import java.util.concurrent.atomic.AtomicInteger;

public class JavaLoad {

  public static void main(String[] args) {
    AtomicInteger count = new AtomicInteger(0);
    int c = count.getAndIncrement();
    System.out.println(c);
  }
}
