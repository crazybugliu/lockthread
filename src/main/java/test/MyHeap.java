package test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 最小堆的实现
 * 类似  {@link PriorityQueue}
 *
 * @param <E>
 */
public class MyHeap<E> {
    private static final int MAX_SIZE = 1 << 15;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private Comparator comparator;
    private Object q[];
    private int size;

    public MyHeap() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public MyHeap(int initialCapacity, Comparator<? extends E> comparator) {
        q = new Object[initialCapacity];
        this.comparator = comparator;
    }

    public MyHeap(Collection<? extends E> c) {
        initFromCollection(c);
    }

    private void initFromCollection(Collection<? extends E> c) {
        q = c.toArray();
        size = c.size();
        heapify();
    }

    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = (E) q[0];

        int s = --size;
        E e = (E) q[s];
        q[s] = null;
        if (s > 0) {
            siftDown(0, e);
        }
        return result;
    }

    public boolean offer(E e) {
        if (size == q.length) {
            grow(q.length + 1);
        }
        size++;
        if (size == 1) {
            q[0] = e;
        } else {
            siftUp(size + 1, e);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, (E) q[i]);
        }
    }

    private void siftDown(int i, E e) {
        if (comparator != null) {
            siftDownComparator(i, e);
        } else {
            siftDownComparable(i, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int i, E e) {
        int half = size >>> 1; // 没有子节点的第一个节点
        int child;
        while (i < half) {
            child = (i << 1) + 1; // 左节点
            int right = child + 1;
            if (right < size && ((Comparable) q[child]).compareTo(q[right]) > 0) {
                child = right;
            }
            if (((Comparable) q[child]).compareTo(e) >= 0) {  //比所有子节点小
                break;
            }
            q[i] = q[child];
            i = child;
        }
        q[i] = e;
    }

    private void siftDownComparator(int i, E e) {

    }

    @SuppressWarnings("unchecked")
    private void siftUp(int i, E e) {
        int parent;
        Comparable c = (Comparable) e;
        while (i > 0) {
            parent = (i - 1) >>> 1; // 父节点
            if (c.compareTo(parent) >= 0) {
                break;
            }
            q[i] = q[parent];
            i = parent;
        }
        q[i] = e;
    }

    private void grow(int minCapacity) {
        if (minCapacity > MAX_SIZE) {
            throw new IndexOutOfBoundsException("reach more than max size!");
        }
        int length = q.length;
        int newSize = length >>> 2;
        newSize = newSize > MAX_SIZE ? MAX_SIZE : size >>> newSize;

        q = Arrays.copyOf(q, newSize);
    }


    public Object[] toArray() {
        return Arrays.copyOf(q, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(q);
    }

    public String threeStr() {
        int width = (int) (Math.log(size) / Math.log(2)) + 1;
        int indent = 2 << (width - 1);
        int gap = 2 << (width - 1);
        StringBuilder sb = new StringBuilder();
        printNSpace(sb, indent);
        sb.append(q[0]);

        int s = 0;
        int layerSize = 0;
        int layerStart = 0;
        int index = 0;
        all:
        while (s < size) {
            sb.append("\n");

            layerSize = 2 << s;
            layerStart = layerSize - 1;
            printNSpace(sb, indent = indent - gap / 2);
            for (int i = 0; i < layerSize; i++) {
                index = layerStart + i;
                if (index > size - 1) {
                    break all;
                }
                sb.append(q[index]);
                printNSpace(sb, gap - String.valueOf(q[index]).length());

            }
            gap = (gap + 1) >>> 1;
            s++;
        }

        return sb.toString();
    }

    private void printNSpace(StringBuilder sb, int n) {

        for (int i = 0; i < n; i++) {
            sb.append(" ");
            //            System.out.print(" ");
        }
    }
}
