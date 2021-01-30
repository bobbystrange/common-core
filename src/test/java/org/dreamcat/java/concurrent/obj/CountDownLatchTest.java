package org.dreamcat.java.concurrent.obj;

import java.util.concurrent.CountDownLatch;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/24
 */
@Ignore
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
