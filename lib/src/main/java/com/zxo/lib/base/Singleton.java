package com.zxo.lib.base;

import java.io.Serializable;

/**
 * 单例模式的几种方式
 * 1、懒汉式
 * 2、饿汉式
 * 3、双重校验锁
 * 4、静态内部类
 */
public class Singleton  implements Serializable {

    // 1、饿汉式
//    private static Singleton instance = new Singleton();
//    private Singleton(){}
//    public static Singleton newInstance(){
//        return instance;
//    }
    // 2、懒汉式
//    private static Singleton instance = null;
//    private Singleton(){}
//    public static Singleton newInstance(){
//        if(null == instance){
//            instance = new Singleton();
//        }
//        return instance;
//    }
    // 3、double-check 双重校验锁 + volatile
//    private static volatile Singleton instance = null;
//    private Singleton(){}
//    public static Singleton getInstance() {
//        if (instance == null) {
//            synchronized (Singleton.class) {
//                if (instance == null) {//2
//                    instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }

    // 4、静态内部类
    private static class SingletonHolder{
        public static Singleton instance = new Singleton();
    }
    private Singleton(){}
    public static Singleton newInstance(){
        return SingletonHolder.instance;
    }

    public Object readResolve() {
        return SingletonHolder.instance;
    }

}
