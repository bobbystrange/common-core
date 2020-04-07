package org.dreamcat.common.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.dreamcat.common.util.ConsoleUtil.printf;

/**
 * Create by tuke on 2020/4/4
 */
public class TimeitTest {

    @Test
    public void testTimeit() {
        for (int count = 1; count <= 8; count++) {
            for (int repeat = 1; repeat <= 8; repeat++) {
                speed(count, repeat, count / 10);
            }
        }
    }

    public void speed(int count, int repeat, int skip) {
        long[] ts = Timeit.ofActions()
                .addVoidAction(() -> {
                    Thread.sleep(1);
                })
                .addVoidAction(() -> {
                    Thread.sleep(3);
                })
                .addVoidAction(() -> {
                    Thread.sleep(7);
                })
                .count(count).repeat(repeat).skip(skip).run();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        printf("count %6d repeat %6d skip %4d cost us %s\n", count, repeat, skip, fmt);
    }
}
