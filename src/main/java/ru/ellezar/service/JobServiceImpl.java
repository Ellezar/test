package ru.ellezar.service;

import ru.ellezar.executor.JobExecutor;
import ru.ellezar.job.ScheduledJob;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

public class JobServiceImpl extends JobService {

    private final PriorityBlockingQueue<ScheduledJob> queue = new PriorityBlockingQueue<>();
    private final JobExecutor executor = new JobExecutor(queue);


    public JobServiceImpl() {
        executor.start();
    }

    @Override
    public void execute(LocalDateTime execOnTime, Callable call) {
        LOGGER.info("execute at {}", execOnTime);
        queue.offer(new ScheduledJob(execOnTime, call));
        synchronized (executor) {
            executor.notify();
        }
    }
}
