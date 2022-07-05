package com.zxo.lib.base.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * 原子累加器 专门用来做数据累加
 */
public class LongAdderDemo {

    public static void main(String[] args) {
        LongAdder adder = new LongAdder();

        List<Thread> ts = new ArrayList<>();

        long begin = System.nanoTime();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(()->{
                for (int j = 0; j < 100000; j++) {
                    adder.increment();
                }
            }));
        }

        ts.forEach(thread -> thread.start());

        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("sum="+adder.longValue()+"  time="+(System.nanoTime()-begin));
    }
}
