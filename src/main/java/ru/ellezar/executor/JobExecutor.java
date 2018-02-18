package ru.ellezar.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ellezar.job.ScheduledJob;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;


public class JobExecutor extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final PriorityBlockingQueue<ScheduledJob> queue;

    public JobExecutor(PriorityBlockingQueue<ScheduledJob> queue) {
        super("JobExecutor");
        this.queue = queue;
    }

    @Override
    public void run() {
        LOGGER.info("Start listening queue");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ScheduledJob job = queue.take();
                long waitInMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), job.getTime());
                if (waitInMillis <= 0) {
                    LOGGER.debug("executing job with id={} and date={}",job.getId(), job.getTime());
                    executor.submit(job.getCall());
                    LOGGER.debug("job with id={} and date={} is done", job.getId(), job.getTime());
                } else {
                    queue.offer(job);
                    synchronized (this) {
                        LOGGER.debug("wait until job's time={}", job.getTime());
                        wait(waitInMillis);
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.debug("Caught InterruptedException={}, start interrupting", e);
                Thread.currentThread().interrupt();
                LOGGER.debug("interrupted");
            }
        }
    }

}
