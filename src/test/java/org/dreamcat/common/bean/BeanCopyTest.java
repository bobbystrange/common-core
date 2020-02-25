package org.dreamcat.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.cglib.beans.BeanCopier;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.util.PrintUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-06-03
 */
public class BeanCopyTest {

    private static PureData prepareData() {
        Random r = new Random();
        return new PureData(
                r.nextInt(128),
                System.currentTimeMillis(),
                new double[]{Math.random(), Math.random(), Math.random()},
                UUID.randomUUID().toString(),
                new Inner(r.nextLong(), new Integer[]{r.nextInt(), r.nextInt()}));
    }

    @Test
    public void test() throws Exception {
        System.out.println("cglib\tspring-beans\tcommon");
        for (int i = 1; i < 1 << 14; i *= 2) {
            speed(100, 10, i);
        }
    }

    @Test
    public void testCglib() {
        PureData source = prepareData();
        PrintUtil.println(BeanUtil.toPrettyString(source));
        PrintUtil.println();

        PureData target1 = new PureData();
        PrintUtil.println(BeanUtil.toPrettyString(target1));
        final BeanCopier copier = BeanCopier.create(
                PureData.class, PureData.class, false);
        copier.copy(source, target1, null);
        PrintUtil.println(BeanUtil.toPrettyString(target1));
        PrintUtil.println();

        PureData target2 = new PureData();
        BeanUtils.copyProperties(source, target2);
        PrintUtil.println(BeanUtil.toPrettyString(target2));
        PrintUtil.println();

        PureData target3 = new PureData();
        BeanUtil.copyProperties(source, target3);
        PrintUtil.println(BeanUtil.toPrettyString(target3));
        PrintUtil.println();

    }

    private void speed(int count, int skip, int repeat) throws Exception {
        final BeanCopier copier = BeanCopier.create(
                PureData.class, PureData.class, false);
        Supplier<Object[]> supplier = () -> new Object[]{prepareData()};

        Timeit timeit = new Timeit()
                .repeat(repeat)
                .count(count)
                .skip(skip)
                .addAction(supplier, args -> {
                    copier.copy(args[0], new PureData(), null);
                })
                .addAction(supplier, args -> {
                    BeanUtils.copyProperties(args[0], new PureData());
                })
                .addAction(supplier, args -> {
                    BeanUtil.copyProperties(args[0], new PureData());
                });
        long[] ts = timeit.runForActions();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        PrintUtil.printf("%07d times copy cost us %s\n", repeat, fmt);
    }

    @NoArgsConstructor
    @AllArgsConstructor
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
