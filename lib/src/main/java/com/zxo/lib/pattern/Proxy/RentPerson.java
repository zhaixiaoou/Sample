package com.zxo.lib.pattern.Proxy;

public class RentPerson implements Person{
  @Override
  public void rentHouse() {
    System.out.println("租客租房成功！");
  }
}
