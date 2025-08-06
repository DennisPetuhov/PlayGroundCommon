package com.example.playgroundcommon.ConCurency;

public class Sync2ThreadsWithObject {


    public static void main(String[] args) {
        Object lock = new Object();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("RUNNABLE 1 STARTED");
                try {
                    Thread.sleep(1000); // Simulate some work
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock.notify();
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    lock.wait();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("RUNNABLE 2 STARTED");
            }
        };
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        thread1.start();

        thread2.start();

    }
}