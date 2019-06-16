package org.dreamcat.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dreamcat.common.core.TimeIt;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-06-03
 */
public class BeanCopyTest {

    @Test
    public void test() throws Exception {
        for(int i=1; i<1<<16; i*=2) {
            speed(100, 10, i);
        }
    }

    private void speed(int count, int skip, int repeat) throws Exception {
        final net.sf.cglib.beans.BeanCopier copier =
                net.sf.cglib.beans.BeanCopier.create(PureData.class, PureData.class, false);
        Random r = new Random();

        Supplier<Object[]> supplier = () -> {
            return new Object[]{
                    new PureData(
                            r.nextInt(128),
                            System.currentTimeMillis(),
                            new double[]{Math.random(), Math.random(), Math.random()},
                            UUID.randomUUID().toString(),
                            new Inner(r.nextLong(), new Integer[]{r.nextInt(), r.nextInt()}))
            };
        };

        TimeIt timeit = new TimeIt()
                .repeat(repeat)
                .count(count)
                .skip(skip)
                .addAction(supplier, args -> {
                    copier.copy(args[0], new PureData(), null);
                })
                .addAction(supplier, args -> {
                    org.springframework.beans.BeanUtils.copyProperties(args[0], new PureData());
                })
                .addAction(supplier, args -> {
                    BeanUtil.copyProperties(args[0], new PureData());
                })
                .addAction(supplier, args -> {
                    org.apache.commons.beanutils.BeanUtils.copyProperties(new PureData(), args[0]);
                });
        long[] ts = timeit.runForActions();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        printf("%07d times copy cost us %s\n", repeat, fmt);
    }

    public static void printf(String format, Object ... args) {
        System.out.printf(format, args);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class PureData {
        int i;
        Long L;
        double[] d;
        String s;
        Inner inner;
    }

    @AllArgsConstructor
    @Data
    static class Inner {
        long l;
        Integer[] ia;
    }
}
