package org.dreamcat.common.core;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/4
 */
@Ignore
public class TimeitTest {

    @Test
    public void testTimeit() {
        for (int count = 1; count <= 8; count++) {
            for (int repeat = 1; repeat <= 8; repeat++) {
                speed(count, repeat, 0);
            }
        }
    }

    public void speed(int count, int repeat, int skip) {
        long[] ts = Timeit.ofActions()
                .addAction(() -> {
                    Thread.sleep(1);
                })
                .addAction(() -> {
                    Thread.sleep(3);
                })
                .addAction(() -> {
                    Thread.sleep(7);
                })
                .count(count).repeat(repeat).skip(skip).run();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        System.out.printf("count %6d repeat %6d skip %4d cost us %s\n", count, repeat, skip, fmt);
    }
}
