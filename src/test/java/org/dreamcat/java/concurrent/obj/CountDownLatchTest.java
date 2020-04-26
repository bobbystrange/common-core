package org.dreamcat.java.concurrent.obj;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Create by tuke on 2020/4/24
 */
public class CountDownLatchTest {

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("count " + i);
                latch.countDown();
            }
        }).start();
        System.out.println("starting");
        latch.await();
        System.out.println("done");
    }
}
