package com.zxo.lib.base.juc;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicReferenceABA {
   static AtomicReference<String> ref = new AtomicReference<>("A");
   // 版本记录
   static AtomicStampedReference<String> ref2 = new AtomicStampedReference<>("A", 0);

   // 变动结果记录
   static AtomicMarkableReference<String> ref3 = new AtomicMarkableReference<>("A", false);
    public static void main(String[] args) throws InterruptedException {

       System.out.println("main start.....");

        String prev = ref.get();
        other();
        Thread.sleep(1000);

        System.out.println("change A->C "+ ref.compareAndSet(prev, "C"));
    }

    private static void other() {
        new Thread(()->{
            System.out.println("change A->B "+ ref.compareAndSet("A", "B"));
        }).start();

        new Thread(()->{
            System.out.println("change B->A "+ ref.compareAndSet("B", "A"));
        }).start();
    }
}
