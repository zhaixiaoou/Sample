package com.zxo.lib.base;

public class ThreadSafe {

    public static int count;

    public static void main(String[] args) throws InterruptedException {
        // 作为锁对象
        // 1、无锁状态
        Object obj = new Object();
        // 也可以作为锁 比上面写法更节省空间 0 + 128（头数据）
        byte[] b = new byte[0];

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i< 10000; i++)
                // 临界区
                {
                    // 多个线程同时对他进行读写
                    // 对象--> 提供锁
                    // JAVA底层实现过程中，对于锁的判断依据全部都在对象上面判定
                    synchronized (b){
                        // 第一个线程启动 b对象为偏向锁
                        count++;
                    }

                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i< 10000; i++){
                    synchronized (b){
                        // 第二个线程启动， b作为轻量级锁
                        count--;
                    }
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(count);
    }

    public static synchronized void decrement(){
        count --;
    }
    ///等价于
    public static void decrement2(){
        // 堆中： class对象
        // 1、内存泄漏的风险
        // a b c 对同一个数据同步， d e f 对另一个数据同步，则a b c d e f 针对同一个对象（类加载的一个Class实例） 会额外锁定
        synchronized (ThreadSafe.class){
            count--;
        }
    }

}
