package org.dreamcat.java.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2019-04-16
 */
@Slf4j
@Ignore
public class ThreadPoolWorkQueueTest {

    @Test(expected = RejectedExecutionException.class)
    public void testArrayBlockingQueue() {
        testWorkQueue(new ArrayBlockingQueue<Runnable>(5));
    }

    @Test(expected = RejectedExecutionException.class)
    public void testSynchronousQueue() {
        testWorkQueue(new SynchronousQueue<Runnable>());
    }

    @Test(expected = RejectedExecutionException.class)
    public void testMyArrayBlockingQueue() {
        testWorkQueue(new MyArrayBlockingQueue<Runnable>(10));
    }

    private void testWorkQueue(BlockingQueue<Runnable> workQueue) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                10,
                0L,
                TimeUnit.SECONDS,
                workQueue);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                executor.shutdownNow();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        for (int i = 0; i < 20; i++) {
            final int seq = i;
            executor.submit(() -> {
                log.info("start {}", seq);
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    log.info("interrupted {}", seq);
                }
                log.info("end {}", seq);
                assert false;
            });
            log.info("submitted {}", seq);
        }

        log.info("poolSize={}, queueSize={}",
                executor.getPoolSize(),
                executor.getQueue().size());
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyArrayBlockingQueue<E> extends ArrayBlockingQueue<E> {

        MyArrayBlockingQueue(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(E e) {
            try {
                super.put(e);
                return true;
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
                return false;
            }
        }
    }
}
