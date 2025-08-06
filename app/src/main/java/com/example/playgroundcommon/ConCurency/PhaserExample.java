package com.example.playgroundcommon.ConCurency;

import java.util.concurrent.Phaser;



import java.util.concurrent.Phaser;

public class PhaserExample {
    public static void main(String[] args) {
        // Создаем Phaser на 3 участника (регистрируем сразу)
        Phaser phaser = new Phaser(3);

        // Запускаем 3 потока
        for (int i = 0; i < 3; i++) {
            new Thread(new Worker(phaser, "Thread-" + i)).start();
        }
    }

    static class Worker implements Runnable {
        private final Phaser phaser;
        private final String name;

        Worker(Phaser phaser, String name) {
            this.phaser = phaser;
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + " начал фазу 1 и ждет остальных");
            phaser.arriveAndAwaitAdvance(); // Ждем все потоки здесь

            System.out.println(name + " начал фазу 2 и снова ждет");
            phaser.arriveAndAwaitAdvance(); // Синхронизация перед фазой 2

            System.out.println(name + " завершил работу");
            phaser.arriveAndDeregister(); // Убираем поток из phaser
        }
    }

    // Main launcher class with full example
    public static class ExampleRunner {
        public static void main(String[] args) {
            // This is a simple launcher that will start the PhaserExample
            System.out.println("Starting PhaserExample demonstration");
            PhaserExample.main(args);
            System.out.println("PhaserExample demonstration launched");
        }
    }
}