package ru.ellezar;

import ru.ellezar.job.TestJob;
import ru.ellezar.service.JobService;

import java.time.LocalDateTime;
import java.util.Queue;

public class TestProcessor implements Runnable {
    private JobService jobService;
    private Queue<Long> sharedQueue;
    private LocalDateTime time;
    private Queue<Long> resultQueue;

    public TestProcessor(JobService jobService, Queue<Long> sharedQueue, LocalDateTime time, Queue<Long> resultQueue) {
        this.jobService = jobService;
        this.sharedQueue = sharedQueue;
        this.time = time;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        if (jobService == null || sharedQueue == null || time == null) {
            return;
        }
        while (!sharedQueue.isEmpty()) {
            Long obj = sharedQueue.poll();
            if (obj == null) break;

            jobService.execute(time, new TestJob(resultQueue, obj));
        }
    }
}
