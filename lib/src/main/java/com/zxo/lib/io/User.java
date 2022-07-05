package com.zxo.lib.io;

import java.io.Serializable;

public class User implements Serializable{

    private static boolean flag = false;

  protected String name;
  protected String age;
  protected String sex;

  private User() throws Exception {
    if (!flag ){
      flag = !flag;
      return;
    }
    throw new Exception("单例模式被破坏");
  }

  private static class UserHandler {
    private static  User user;

    static {
      try {
        user = new User();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static User getInstance(){
    return UserHandler.user;
  }

//  protected Object readResolve(){
//    System.out.println("调用了readResolve方法！");
//    return UserHandler.user;
//  }

  public User(String name, String age, String sex) {

    this.name = name;
    this.age = age;
    this.sex = sex;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

//  @Override
//  public String toString() {
//    return "User{" + "name='" + name + '\'' + ", age='" + age + '\'' + ", sex='" + sex + '\'' + '}';
//  }
}
