package test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;


public class MyHeapTest {

    List<Integer> list;

    @Before
    public void before() throws Exception {
        list = Arrays.asList(6, 8, 3, 1, 6, 2, 9, 6, 0, 2, 12, 43, 66, 11, 88, 6, 8, 3, 1, 6, 2, 9, 6, 0, 2, 12, 43, 66, 11, 88);
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testInit() {
        MyHeap<Integer> myHeap = new MyHeap<>(list);
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(list);

        Integer[] ha = (Integer[]) myHeap.toArray();
        Integer[] pa = priorityQueue.toArray(new Integer[0]);

        for (int i = 0; i < list.size(); i++) {
            if(ha[i].intValue() != pa[i].intValue()) {
                throw new RuntimeException("wrong heap");
            }
        }

    }

    @Test
    public void testPoll() throws Exception {
        //TODO: Test goes here...
    }

    @Test
    public void testOffer() throws Exception {
        //TODO: Test goes here...
    }



} 
