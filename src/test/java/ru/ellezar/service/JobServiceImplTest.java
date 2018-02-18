package ru.ellezar.service;

import org.junit.Before;
import org.junit.Test;
import ru.ellezar.TestProcessor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

public class JobServiceImplTest {

    private JobServiceImpl jobService;
    private Queue<Long> result;

    @Before
    public void init() {
        jobService = new JobServiceImpl();
        result = new ConcurrentLinkedQueue<>();
    }

    @Test
    public void testOneJob() throws InterruptedException {
        jobService.execute(LocalDateTime.now(), () -> result.add(1l));
        Thread.sleep(3000l);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testJobInThePast() throws InterruptedException {
        jobService.execute(LocalDateTime.now().minusSeconds(100),() -> result.add(1l));
        Thread.sleep(1000l);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testWithDiffDelay() throws InterruptedException {
        List<Long> delays = LongStream.range(-3l, 3l).boxed().collect(Collectors.toList());
        for (int i = 0; i < delays.size(); i++) {
            jobService.execute(LocalDateTime.now().minusSeconds(delays.get(i)),() -> result.add(1l));
        }
        Thread.sleep(4000l);
        assertEquals(result.size(), delays.size());
    }

    @Test
    public void testWithSeveralThreads() throws InterruptedException {
        int testCount = 1000;
        List<Long> values = LongStream.range(0, testCount).boxed().collect(Collectors.toList());
        Queue<Long> sharedQueue = new ConcurrentLinkedQueue<>(values);
        LocalDateTime time = LocalDateTime.now().plusSeconds(1l);
        for (int i = 0; i < 3; i++) {
            new Thread(new TestProcessor(jobService, sharedQueue, time, result), "Thread number="+i).start();
        }
        Thread.sleep(3000l);
        assertEquals(testCount,result.size());
    }

    @Test
    public void testWithSeveralThreadsAndDiffDelay() throws InterruptedException {
        int testCount = 1000;
        List<Long> values = LongStream.range(0, testCount).boxed().collect(Collectors.toList());
        Queue<Long> sharedQueue = new ConcurrentLinkedQueue<>(values);
        for (int i = 0; i < 3; i++) {
            LocalDateTime time = LocalDateTime.now().plusSeconds(1l);
            new Thread(new TestProcessor(jobService, sharedQueue, time, result), "Thread number="+i).start();
        }
        Thread.sleep(3000l);
        assertEquals(testCount,result.size());
    }

}