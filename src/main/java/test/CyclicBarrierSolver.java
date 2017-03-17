package test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 参考类CyclicBarrier的 java api 说明： http://tool.oschina.net/apidocs/apidoc?api=jdk-zh
 */
class CyclicBarrierSolver {
    final int N;
    final float[][] data;
    final CyclicBarrier barrier;

    volatile boolean isDone = false;

    private boolean done() {
        return isDone;
    }

    private void setDone() {
        isDone = true;
    }

    class Worker implements Runnable {
        int myRow;

        Worker(int row) {
            myRow = row;
        }

        public void run() {
            while (!done()) {
                processRow(myRow);

                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }

        void processRow(int myRow) {
            System.out.println("process my row " + myRow);
        }
    }

    public CyclicBarrierSolver(float[][] matrix) {
        data = matrix;
        N = matrix.length;
        final AtomicInteger doneCount = new AtomicInteger(0);
        barrier = new CyclicBarrier(N,
                new Runnable() {
                    public void run() {
                        mergeRows(data);
                        int count = doneCount.incrementAndGet();
                        System.out.println("done count " + count);
                        if (count >= 3) {
                            setDone();
                        }
                    }

                    private double mergeRows(float[][] matrix) {
                        return 0;
                    }
                });
        for (int i = 0; i < N; ++i)
            new Thread(new Worker(i)).start();


        while (doneCount.get() < 3) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        float[][] m = new float[10][10];
        CyclicBarrierSolver solver = new CyclicBarrierSolver(m);
    }
}