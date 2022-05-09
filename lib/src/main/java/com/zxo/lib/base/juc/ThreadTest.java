package com.zxo.lib.base.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程 依次打印
 */
public class ThreadTest {

    private Lock lock = new ReentrantLock();

    private Condition numCondition = lock.newCondition();
    private Condition letterCondition = lock.newCondition();

    private byte[] objectLock = new byte[0];

    private  int times;

    private int state = 1;

    public ThreadTest(int times){
        this.times = times;
    }

    /**
     * 通过Lock形式 实现依次打印
     * @param letter
     * @param targetNum
     */
    private void printerLockABC(String letter, int targetNum){
        for (int i = 0; i < times; ){
            lock.lock();
            if (state % 3 == targetNum){
                state ++ ;
                i++;
                System.out.print(letter);
            }
            lock.unlock();
        }
    }

    /**
     * 采用object wait和notify的方式实现 依次打印
     * @param letter
     * @param targetNum
     */
    private void printerWaitABC(String letter, int targetNum){
        for (int i = 0; i< times; ){
            synchronized (objectLock) {
                if (state %3 == targetNum){
                    state ++;
                    i++;
                    System.out.print(letter);
                    objectLock.notifyAll();
                } else {
                    try {
                        objectLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * lock 循环奇偶数
     * @param threadName
     * @param targetNum
     */
    private void printerLockNum(String threadName, int targetNum){

        while (state < times){
            lock.lock();
            if (state % 2 == targetNum){
                System.out.println("threadName"+threadName+"-"+state);
                state++;
            }

            lock.unlock();
        }
    }

    private void printerWaitNum(String threadName, int targetNum){
        while (state < times ){
            synchronized (objectLock){
                if (state % 2 == targetNum){
                    System.out.println("threadName"+threadName+"-"+state);
                    state++;
                }
            }
        }
    }

    private void printerLockA1B2(){

        synchronized (objectLock){
            for (int i=0; i<26; i++){
                if (Thread.currentThread().getName().equals("num")){
                    System.out.print(i+1);
                    objectLock.notifyAll();
                    try {
                        objectLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (Thread.currentThread().getName().equals("letter")){
                    System.out.print((char)('A'+i));
                    objectLock.notifyAll();
                    try {
                        objectLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            objectLock.notifyAll();
        }


    }

    static Thread numThread, letterThread;

    public static void main(String[] args) {
        ThreadTest test = new ThreadTest(20);
//        new Thread(()->{
////            test.printerLockABC("A",0 );
//            test.printerWaitABC("A",0 );
//        }).start();
//
//        new Thread(()->{
////            test.printerLockABC("B",1 );
//            test.printerWaitABC("B",1 );
//        }).start();
//        new Thread(()->{
////            test.printerLockABC("C",2 );
//            test.printerWaitABC("C",2 );
//        }).start();

//        new Thread(()->{
//            test.printerWaitNum("odd",1);
//        }).start();
//        new Thread(()->{
//            test.printerWaitNum("event",0);
//        }).start();

//        new Thread(test::printerLockA1B2, "num").start();
//        new Thread(test::printerLockA1B2, "letter").start();


        lockSupportTestNumAndLetter();
    }

    private static void lockSupportTestNumAndLetter() {

        numThread = new Thread(()->{
            for (int i =0;i< 26; i++){
                System.out.print(i+1);
                LockSupport.park();
                LockSupport.unpark(letterThread);
            }
        });


        letterThread = new Thread(()->{
            for (int i=0; i< 26; i++){
                System.out.print((char)('A'+i));
                LockSupport.unpark(numThread);
                LockSupport.park();
            }
        });



        numThread.start();
        letterThread.start();
    }
}
