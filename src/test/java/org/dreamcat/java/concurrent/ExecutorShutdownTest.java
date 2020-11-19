package org.dreamcat.java.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * Create by tuke on 2019-03-27
 */
public class ExecutorShutdownTest {

    private static final AtomicInteger lineno = new AtomicInteger(1);

    @Test
    public void test() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(22, 44, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(22));

        long timestamp = System.currentTimeMillis();
        for (int i = 0; i < 16; i++) {
            final int k = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(10L);
                    System.out.println(lineno.getAndIncrement() + " " + k);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        System.out.println(
                lineno.getAndIncrement() + " " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    @Test
    public void main() {
        test();
        try {
            Thread.sleep(1000L);
            System.out.println(lineno.getAndIncrement() + " " + "Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
