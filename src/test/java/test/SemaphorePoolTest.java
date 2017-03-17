package test;

import java.util.Random;

public class SemaphorePoolTest {
    public static void main(String[] args) {
        final SemaphorePool semaphorePool = new SemaphorePool();

        final Random random = new Random();

        class Worker implements Runnable {
            public void run() {
                int item = -1;
                try {
                    int sleepMils = random.nextInt(2000);
//                    System.out.println(Thread.currentThread().getName() + "====> sleep " + sleepMils);
                    Thread.sleep(sleepMils);

                    item = semaphorePool.getItem();
                    System.out.println(Thread.currentThread().getName() + "====> get  " + item);

                    sleepMils = random.nextInt(2000);
                    Thread.sleep(sleepMils);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (item > 0) {
                        try {
                            semaphorePool.putItem(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // ignore
                        }
                    }
                }
            }
        }

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                        System.out.println();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        for (int i = 0; i < 100; i++) {
            new Thread(new Worker()).start();
        }
    }
}
