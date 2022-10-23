package com.zxo.lib.io;

import java.io.Serializable;

public class Person extends User implements Serializable {

  private transient String num;

  private static String ss ;

  public Person(String name, String age, String sex, String num) {
    super(name, age, sex);
    this.num = num;
    ss = "zhaixiaoou";
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  @Override
  public String toString() {
    return "Person{" + "num='" + num + '\'' + ", name='" + name + '\'' + ", age='" + age + '\'' + ", sex='" + sex + '\''+ ", ss='" + ss + '\''  + '}';
  }

}
