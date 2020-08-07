package org.dreamcat.common.util;

import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.function.ThrowableSupplier;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static org.dreamcat.common.util.FormatUtil.printf;
import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/4
 */
public class BinSortSpeedTest {
    // binSort reached fastest when size grew to 64;
    @Test
    public void testBinSortSpeed() {
        println("\t\t\t\t\t\t\t\t\t   bin \t  bubble \t select \t  insert");
        for (int size = 2; size < (1 << 8); size <<= 1) {
            for (int repeat = 2; repeat <= (1 << 8); repeat = repeat << 1) {
                speed(size, repeat);
            }
        }
    }

    private void speed(int size, int repeat) {
        ThrowableSupplier<LinkedList<Integer>> supplier = () -> {
            LinkedList<Integer> list = new LinkedList<>();
            for (int i = 0; i <= size; i++) {
                list.add(i);
            }
            return list;
        };

        long[] ts = Timeit.ofActions()
                .addUnaryAction(supplier, list -> {
                    SortUtil.binSort(list, it -> it % 10, 10);
                })
                .addUnaryAction(supplier, SortUtil::bubbleSort)
                .addUnaryAction(supplier, SortUtil::selectSort)
                .addUnaryAction(supplier, SortUtil::insertSort)
                .count(12).repeat(repeat).skip(2).run();
        String fmt = Arrays.stream(ts)
                .mapToObj(it -> String.format("%09.3f", it / 1000.))
                .collect(Collectors.joining("\t"));
        printf("size %6d repeat %6d cost us %s\n", size, repeat, fmt);
    }
}
