package org.dreamcat.java.concurrent.speed;

import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.core.forkjoin.ConcurrentVariance;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.util.Arrays;

/**
 * Create by tuke on 2020/8/14
 */
public class SteamReduceTest {

    @Test
    public void test() {
        double[] data = Arrays.stream(ArrayUtil.rangeOf(0, 100_000_000)).mapToDouble(it -> RandomUtil.rand()).toArray();

        for (int i = 1; i < 1000; i *= 8) {
            String ts = Timeit.ofActions().repeat(i).count(10).skip(2)
                    .addAction(() -> {
                        double sum = 0;
                        for (double n : data) sum += n;
                    }).addAction(() -> {
                        Arrays.stream(data).parallel().sum();
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 100);
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 1000);
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 10000);
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 100000);
                    })
                    .runAndFormatMs();
            System.out.printf("%6d \t %s\n", i, ts);
        }
    }

    @Test
    public void testHuge() {
        double[] data = Arrays.stream(ArrayUtil.rangeOf(0, 100_000_000)).mapToDouble(it -> RandomUtil.rand()).toArray();

        for (int i = 1; i < 10; i++) {
            String ts = Timeit.ofActions().repeat(i)
                    .addAction(() -> {
                        double sum = 0;
                        for (int k = 0, size = data.length; k < size; k++) {
                            sum += data[k];
                        }
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 1000);
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 10000);
                    })
                    .addAction(() -> {
                        ConcurrentVariance.mean(data, 100000);
                    })
                    .runAndFormatMs();
            System.out.printf("%6d \t %s\n", i, ts);
        }
    }
}
