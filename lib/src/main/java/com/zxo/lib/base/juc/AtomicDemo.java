package com.zxo.lib.base.juc;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {

    static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {

        LockCas lock = new LockCas();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
//                lock.lock();
//                count++;
//                lock.unlock();
                count.incrementAndGet();
            }
        });


       Thread thread2 =  new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
//                lock.lock();
//                count++;
//                lock.unlock();
                count.incrementAndGet();
            }
        });

       thread1.start();
       thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("count="+count);
    }
}
