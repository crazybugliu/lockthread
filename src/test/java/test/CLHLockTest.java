package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CLHLockTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void test() throws Exception {
        CLHLock lock = new CLHLock();
        List<Worker> workers = new ArrayList<>(10);
        for (int i = 0; i < 100; i++) {
            Worker w = new Worker(lock);
            workers.add(w);
            w.start();
        }

        workers.forEach(worker -> {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    class Worker extends Thread {
        private CLHLock lock;

        public Worker(CLHLock lock) {
            this.lock = lock;
        }

        @Override public void run() {
            lock.lock();
            System.out.println(System.nanoTime() + ": " + Thread.currentThread().getName() + " run.");
            lock.unlock();
        }
    }


} 
