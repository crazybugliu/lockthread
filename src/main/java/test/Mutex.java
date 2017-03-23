package test;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 基于AQS的互斥锁的简单实现
 * 详细的可见jdk中AQS类的java api说明：http://tool.oschina.net/apidocs/apidoc?api=jdk-zh
 * 参考：http://ifeve.com/aqs-3/#usage
 */
class Mutex {
    class Sync extends AbstractQueuedSynchronizer {
		public boolean tryAcquire(int ignore) {
			return compareAndSetState(0, 1);
		}
		public boolean tryRelease(int ignore) {
			setState(0); return true;
		}
	}

	private final Sync sync = new Sync();
	public void lock() { sync.acquire(0); }
	public void unlock() { sync.release(0); }
}