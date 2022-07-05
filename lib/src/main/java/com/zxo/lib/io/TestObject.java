package com.zxo.lib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TestObject {

  public static void main(String[] args) throws Exception {

    User user = User.getInstance();
//    User user = new Person("zxo", "18", "1", "123");

//    ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
//    Constructor constructor = reflectionFactory.newConstructorForSerialization(User.class ,  Object.class.getDeclaredConstructor());
//    constructor.setAccessible(true);
//    User user= (User) constructor.newInstance();

//    try {
//      Class clz = Class.forName(User.class.getName());
//      User user = (User) clz.newInstance();
//      System.out.println(user.toString());
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      e.printStackTrace();
//    } catch (InstantiationException e) {
//      e.printStackTrace();
//    }
//
    System.out.println(user.toString());
    try {
      File file = new File("obj.txt");
      if (!file.exists()) {
        file.createNewFile();
      }

      FileOutputStream out = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(out);
      oos.writeObject(user);
      oos.close();
      out.close();

      FileInputStream ins  = new FileInputStream(file);

      ObjectInputStream ios = new ObjectInputStream(ins);
      User newUser = (User) ios.readObject();
      ios.close();
      System.out.println(newUser.toString());
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
