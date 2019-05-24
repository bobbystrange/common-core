package org.dreamcat.common.java.concurrent;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2019-03-27
 */
public class ExecutorShutdownTest {

    private static volatile int lineno = 1;

    @Test
    public void test(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(22, 44, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(22));

        long timestamp = System.currentTimeMillis();
        for (int i=0; i < 16; i++) {
            final int k = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(10L);
                    System.out.println(lineno++ + " " + k);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        System.out.println(lineno++ + " " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    @Test
    public void main(){
        test();
        try {
            Thread.sleep(1000L);
            System.out.println(lineno++ + " " +"Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
