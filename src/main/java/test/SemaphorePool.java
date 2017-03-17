package test;

import java.util.concurrent.Semaphore;

/**
 * 信号量的使用示例
 * 参考类 Semaphore 的 java api：http://tool.oschina.net/apidocs/apidoc?api=jdk-zh
 */
class SemaphorePool {
    private static final int MAX_AVAILABLE = 5;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    public int getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(int x) {
        if (markAsUnused(x))
            available.release();
    }

    // Not a particularly efficient data structure; just for demo

    protected int[] items = {1, 2, 3, 4, 5};
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    protected synchronized int getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return 0; // not reached
    }

    protected synchronized boolean markAsUnused(int item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

}