package ru.ellezar.job;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledJob implements Comparable<ScheduledJob> {

    private static final AtomicLong idSequence = new AtomicLong(0);

    private final long id;
    private final LocalDateTime time;
    private final Callable call;

    public ScheduledJob(LocalDateTime time, Callable call) {
        this.id = idSequence.getAndIncrement();
        this.time = time;
        this.call = call;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Callable getCall() {
        return call;
    }

    @Override
    public int compareTo(ScheduledJob job) {
        int result = this.time.compareTo(job.time);
        if (result == 0) {
            result = Long.compare(this.id, job.id);
        }
        return result;
    }
}
