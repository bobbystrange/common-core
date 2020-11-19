package org.dreamcat.java.lang;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.dreamcat.common.core.Timeit;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/18
 */
public class ObjectTest {

    @Test
    public void testHashCode() {
        for (int i = 2; i < 10_000; ) {
            i *= 2;
            long[] tss = Timeit.ofActions()
                    .addAction(() -> {
                        Long n = System.currentTimeMillis();
                        if (n.hashCode() > 0) ;
                    })
                    .addAction(() -> {
                        long n = System.currentTimeMillis();
                        if (n > 0) ;
                    })
                    .repeat(i).count(16).skip(4).run();
            System.out.printf("%4d\t%s\n", i, Arrays.toString(tss));
        }
    }

    @Test
    public void testWait() throws InterruptedException {
        final WaitNotify waitNotify = new WaitNotify(false, 1);
        new Thread(new PrintOddNumber(waitNotify)).start();
        new Thread(new PrintEvenNumber(waitNotify)).start();
        Thread.sleep(1000);
    }

    @AllArgsConstructor
    static class WaitNotify {

        private volatile boolean even;
        private int n;
    }

    @AllArgsConstructor
    static class PrintEvenNumber implements Runnable {

        private final WaitNotify waitNotify;

        @Override
        public void run() {
            while (waitNotify.n <= 100) {
                synchronized (WaitNotify.class) {
                    if (waitNotify.even) {
                        System.out.println(waitNotify.n);
                        waitNotify.n++;
                        waitNotify.even = false;
                        WaitNotify.class.notify();
                    } else {
                        try {
                            WaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }

            }
        }
    }

    @AllArgsConstructor
    static class PrintOddNumber implements Runnable {

        private final WaitNotify waitNotify;

        @Override
        public void run() {
            while (waitNotify.n <= 100) {
                synchronized (WaitNotify.class) {
                    if (!waitNotify.even) {
                        System.out.println(waitNotify.n);
                        waitNotify.n++;
                        waitNotify.even = false;
                        WaitNotify.class.notify();
                    } else {
                        try {
                            WaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }

            }
        }
    }
}
