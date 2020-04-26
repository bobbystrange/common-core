package org.dreamcat.java.lang;

import org.dreamcat.common.core.Timeit;
import org.junit.Test;

import java.util.Arrays;

/**
 * Create by tuke on 2020/4/21
 */
public class ArrayTest {

    @Test
    public void testCopyOf() {
        for (int i = 2; i < 10_000; ) {
            i *= 2;
            int[] a = new int[i];
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        Arrays.copyOf(a, a.length);
                    })
                    .addAction(() -> {
                        int[] b = new int[a.length];
                        System.arraycopy(a, 0, b, 0, a.length);
                    })
                    .repeat(i).count(16).skip(4).run();
            System.out.printf("%4d\t%s\n", i, Timeit.formatUs(ts, "\t"));
        }
    }
}
