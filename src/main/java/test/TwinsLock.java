package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 一个示例，用于展现集成AQS实现一个特殊的锁
 */
public class TwinsLock implements Lock {


    class Sync extends AbstractQueuedSynchronizer {
        public Sync(int num) {
            setState(num);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            for (; ; ) {
                int current = getState();
                int newState = current - arg;
                if (newState < 0 || compareAndSetState(current, newState)) {
                    return newState;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (; ; ) {
                int current = getState();
                int newState = current + arg;
                if (compareAndSetState(current, newState)) {
                    return true;
                }
            }
        }
    }


    private final Sync sync = new Sync(2);

    public void lock() {
        sync.acquireShared(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    public void unlock() {
        sync.releaseShared(1);
    }

    public Condition newCondition() {
        return null;
    }

}
