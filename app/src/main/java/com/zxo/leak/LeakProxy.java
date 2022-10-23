package com.zxo.leak;

public class LeakProxy<T> {

  public static <T>  T create(T iLeak) {
    return iLeak;
  }
}
