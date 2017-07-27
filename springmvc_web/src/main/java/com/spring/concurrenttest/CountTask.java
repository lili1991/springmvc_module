package com.spring.concurrenttest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by liangll18922 on 2017/5/17.
 */
public class CountTask extends RecursiveTask<Integer>{
    private static final int THRESHOLD=2;//阈值
    private int start;
    private int end;

    public CountTask(int start,int end){
        this.start=start;
        this.end=end;
    }


    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        }else{
            int middle=(start+end)/2;
            CountTask leftTask=new CountTask(start,middle);
            CountTask rigthTask=new CountTask(middle,end);
            leftTask.fork();
            rigthTask.fork();

            int leftResult=leftTask.join();
            int rightResult=rigthTask.join();

            sum=leftResult+rightResult;
        }

        return sum;
    }

    public static void main(String[] args){
        Long[] ids={Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)};

        System.out.println(ids.toString());

        ForkJoinPool forkJoinPool=new ForkJoinPool();

        CountTask task=new CountTask(1,4);

        Future<Integer> result=forkJoinPool.submit(task);

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
