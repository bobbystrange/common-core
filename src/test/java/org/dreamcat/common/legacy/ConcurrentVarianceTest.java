package org.dreamcat.common.legacy;

import java.util.Arrays;
import org.dreamcat.common.Timeit;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/8/14
 */
@Ignore
public class ConcurrentVarianceTest {

    @Test
    public void test() {
        double[] data = Arrays.stream(ArrayUtil.rangeOf(0, 100_000))
                .mapToDouble(it -> RandomUtil.rand()).toArray();
        int size = data.length;
        // mean
        double sum = 0;
        for (double n : data) {
            sum += n;
        }
        double mean = sum / size;

        // variance
        sum = 0;
        for (double n : data) {
            sum += (n - mean) * (n - mean);
        }
        double variance = sum / size;

        System.out.println(mean);
        System.out.println(ConcurrentVariance.mean(data, 100));

        System.out.println(variance);
        System.out.println(ConcurrentVariance.variance(data, 100));

        Timeit timeit = Timeit.ofActions().count(10).skip(2);
        timeit.addAction(() -> {

        });
        for (int i = 8; i <= 10_000; i *= 8) {
            int finalI = i;
            timeit.addAction(() -> {
                double v = ConcurrentVariance.variance(data, finalI);
                System.out.printf("%06d \t %f\n", finalI, v);
            });
        }

        System.out.printf("%s\n", timeit.runAndFormatMs());
    }

    @Test
    public void testSpeed() {
        double[] data = Arrays.stream(ArrayUtil.rangeOf(0, 100_000_000))
                .mapToDouble(it -> RandomUtil.rand()).toArray();

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
    public void testHugeSpeed() {
        double[] data = Arrays.stream(ArrayUtil.rangeOf(0, 100_000_000))
                .mapToDouble(it -> RandomUtil.rand()).toArray();

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
