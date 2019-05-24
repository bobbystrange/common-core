package org.dreamcat.common.java.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2019-04-16
 */
@Slf4j
public class RejectedExecutionHandlerTest {

    @Test(expected = RejectedExecutionException.class)
    public void testAbortPolicy() {
        testRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    }

    @Test
    public void testDiscardOldestPolicy() {
        testRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    @Test
    public void testDiscardPolicy() {
        testRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    }

    @Test
    public void testCallerRunsPolicy() {
        testRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private void testRejectedExecutionHandler(RejectedExecutionHandler handler) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                10,
                0L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(5),
                handler);

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

}
