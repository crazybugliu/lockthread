package test.executor;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ExecutorTest {


    @Test
    public void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Set<Callable<String>> callables = new HashSet<>();

        callables.add(() -> {
            System.out.println("Task 1 done");
            return "Task 1";
        });
        callables.add(() -> {
            System.out.println("Task 2 done");
            return "Task 2";
        });
        callables.add(() -> {
            System.out.println("Task 3 done");
            return "Task 3";
        });

        String result = executorService.invokeAny(callables);

        System.out.println("result = " + result);

        executorService.shutdown();
    }

    @Test
    public void testInvokeAll() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Set<Callable<String>> callables = new HashSet<>(3);
        callables.add(() -> "Task 1");
        callables.add(() -> "Task 2");
        callables.add(() -> {
            while (true){
                try {
                    if(Thread.currentThread().isInterrupted()){
                        throw new InterruptedException("task 3");
                    }
                    Thread.sleep(10);
                }
                catch (InterruptedException e){
                    System.out.println("Task 2 InterruptedException happened: " + e.getMessage());
                    break;
//                    return "Task 3 interrupted";
                }
            }
            return "Task 3";
        });
        List<Future<String>> futures = executorService.invokeAll(callables);
        executorService.shutdown();
        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}
