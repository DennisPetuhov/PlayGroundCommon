package com.example.playgroundcommon.ConCurency;

class HelloWorld {
    public static void main(String[] args) {
        Example example = new Example();
      
          Thread thread1 = new Thread(() -> {
            example.foo();
        });
        thread1.start();
        
         Thread thread2 = new Thread(() -> {
            example.bar();
        });
        thread2.start();
    }
}
class Example {
    private final Object mLock = new Object();
    public void foo() {
        synchronized (mLock) {
            method1();
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            method2();
        }
    }

    public void bar() {
        synchronized (mLock) {
            method3();
            mLock.notifyAll();
            method4();
        }
    }


    public  void method1(){
        System.out.println("1");
    }
    public  void method2(){
        System.out.println("2");
    }
    public  void method3(){
        System.out.println("3");
    }
    public  void method4(){
        System.out.println("4");
    }
}