package test.forkjoin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinTest {

    ForkJoinPool forkJoinPool = new ForkJoinPool(8);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
        forkJoinPool.shutdown();
    }

    @Test
    public void testRecursiveAction() throws Exception {
        MyRecursiveAction recursiveAction = new MyRecursiveAction(24);
        forkJoinPool.invoke(recursiveAction);
    }

    @Test
    public void testRecursiveTask() throws Exception {
        long s = System.nanoTime();
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);
        long mergedResult = forkJoinPool.invoke(myRecursiveTask);
        System.out.println("mergedResult = " + mergedResult);
        System.out.println((System.nanoTime()-s)/1000000 + " milli seconds.");
    }


} 
