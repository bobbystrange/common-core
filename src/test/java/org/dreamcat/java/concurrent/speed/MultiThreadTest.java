package org.dreamcat.java.concurrent.speed;

import org.dreamcat.common.core.Timeit;
import org.junit.Test;

/**
 * Create by tuke on 2020/7/8
 */
public class MultiThreadTest {

    @Test
    public void test() {
        System.out.println("\t\t serial \t concurrency");
        for (int i = 2; i < 10000_0000; i *= 2) {
            int count = i;
            String tss = Timeit.ofActions()
                    .addAction(() -> serial(count))
                    .addAction(() -> concurrency(count))
                    .runAndFormatUs();
            System.out.printf("%08d\t%s\n", count, tss);
        }

    }

    private void serial(int count) {
        plus_or_minus(count);
        minus(count);
    }

    private void concurrency(int count) throws InterruptedException {
        Thread thread = new Thread(() -> {
            plus_or_minus(count);
        });
        thread.start();
        minus(count);
        thread.join();
    }

    private void plus_or_minus(int count) {
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += i % 2 == 0 ? 1 : -1;
        }
    }

    private void minus(int count) {
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
    }
}
