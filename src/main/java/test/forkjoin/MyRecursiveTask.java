package test.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
    
    
public class MyRecursiveTask extends RecursiveTask<Long> {

    private long workLoad = 0;

    public MyRecursiveTask(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected Long compute() {

        //if work is above threshold, break tasks up into smaller tasks
        if(this.workLoad > 16) {
            System.out.println("Splitting workLoad : " + this.workLoad + getThreadName() );

            List<MyRecursiveTask> subtasks = createSubtasks();
            subtasks.forEach(ForkJoinTask::fork);

            long result = 0;
            for(MyRecursiveTask subtask : subtasks) {
                result += subtask.join();
            }
            return result;

        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad + getThreadName() );
            return workLoad * 3;
        }
    }

    private List<MyRecursiveTask> createSubtasks() {
        return Arrays.asList(
                new MyRecursiveTask(this.workLoad / 2),
                new MyRecursiveTask(this.workLoad / 2));
    }

    private String getThreadName() {
        return  "\t\t\t\t\t" + Thread.currentThread().getName();
    }
}