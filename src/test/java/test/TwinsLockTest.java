package test;

public class TwinsLockTest {
    public static void main(String[] args) throws InterruptedException {
        final TwinsLock lock = new TwinsLock();
        class Worker implements Runnable {
            public void run() {
                try {
                    lock.lock();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                     lock.unlock();
                }

            }
        }

        for (int i = 0; i < 100; i++) {
            new Thread(new Worker()).start();
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

        Thread.sleep(20000);
    }
}
