package com.zxo.lib.base.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 利用CAS理论 来实现锁功能的效果
 */
public class LockCas {
    // 0 没加锁 1加锁
    private AtomicInteger state = new AtomicInteger(0);

    public void lock(){
        while (true){
            if (state.compareAndSet(0,1)){
                break;
            }
        }
    }

    public void unlock(){
        state.set(0);
    }
}
