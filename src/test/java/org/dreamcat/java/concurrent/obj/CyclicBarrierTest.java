package org.dreamcat.java.concurrent.obj;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/24
 */
@Ignore
public class CyclicBarrierTest {

    @Test
    public void test() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println("done");
        });

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("count " + finalI);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(1000);
    }
}
