package org.dreamcat.common.core.forkjoin;

import java.util.Arrays;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/8/14
 */
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
}
