package test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLH锁实现
 * 和doug lea改进后的CLH是不一样的
 * 和doug lea文章里描述的也不一样：http://ifeve.com/aqs-2
 * 此实现或许还有些问题
 * 参考：https://segmentfault.com/a/1190000004935026
 */
public class CLHLock {
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;

    public CLHLock() {
        tail = new AtomicReference<>(new QNode());
        myPred = ThreadLocal.withInitial(() -> null);
        myNode = ThreadLocal.withInitial(QNode::new);
    }

    public void lock() {
        QNode qnode = myNode.get();
        qnode.locked = true;
        QNode pred = tail.getAndSet(qnode);
        myPred.set(pred);
        while (pred.locked) {
            /*
             * 若只是空循环会占用CPU
             * 可以Thread.yield()让其他线程有更多的机会被调度到
             * 可以Thread.sleep(time),但time时间的大小其实不好确定
             */
        }
    }

    public void unlock() {
        QNode qnode = myNode.get();
        qnode.locked = false;
        /*
         * 帮助GC.
         * 最终每个释放锁的节点其实都是将当前节点设置为了头节点（也就是最开始初始化的tail节点）
         * 这样的话下一个节点保留的 myPred 这个对象引用就成为了头节点， 从而，当前释放锁的节点在队列中没有了引用
         * 即当前节点对于这个FIFO完成了出队动作
         */
        myNode.set(myPred.get());
    }

    class QNode {
        /*
         * 这里必须使用volatile，
         * 不然unlock设置的locked=false，
         * 在前一个节点可能看不到这个值造成了lock中while的死循环
         */
        volatile boolean locked = false;
    }
}