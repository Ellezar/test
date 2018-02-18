package ru.ellezar.job;

import java.util.Queue;
import java.util.concurrent.Callable;

public class TestJob implements Callable {
    private Queue<Long> resultQueue;
    private long value;

    public TestJob(Queue<Long> resultQueue, long value) {
        this.resultQueue = resultQueue;
        this.value = value;
    }


    @Override
    public Object call() throws Exception {
        resultQueue.offer(value);
        return null;
    }
}
