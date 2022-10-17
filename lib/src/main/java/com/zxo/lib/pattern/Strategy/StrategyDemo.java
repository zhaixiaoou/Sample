package com.zxo.lib.pattern.Strategy;

import java.util.Scanner;

/**
 * 策略模式使用场景
 * 同一个算法，不同的实现方式
 * 定义了很多行为，通过if-else来进行行为选择
 *
 * 与工厂模式的区别
 * 1、用途不一样。
 *    工厂模式是创建型模式，用于创建对象。
 *    策略模式是行为型模式，让一个对象在多种行为中选择一种
 */
public class StrategyDemo {

  public static void main(String[] args) {
    System.out.println("请输入品牌方");
    ModelFactory modelFactory = new ModelFactory("vivo");
    String ssid = modelFactory.getSsid();
    System.out.println("品牌方ssid="+ssid);
  }
}
