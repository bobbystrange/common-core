package org.dreamcat.common.util;

import org.dreamcat.common.core.Timeit;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/4/19
 */
public class ComparatorUtilTest {

    @SuppressWarnings({"unchecked"})
    private static <T> int compareTo(T a, T b) {
        if (a == null && b == null) return 0;
        // null always less than not-null
        if (a == null) return -1;
        if (b == null) return 1;

        if (a instanceof Comparable) {
            return ((Comparable<T>) a).compareTo(b);
        }

        int offset = a.hashCode() - b.hashCode();
        if (offset != 0) return offset;

        return a.getClass().hashCode() - b.getClass().hashCode();
    }

    @SuppressWarnings({"unchecked"})
    private static <T> int compareTo2(T a, T b) {
        if (a instanceof Comparable) {
            return ((Comparable<T>) a).compareTo(b);
        }

        int offset = a.hashCode() - b.hashCode();
        if (offset != 0) return offset;

        return a.getClass().hashCode() - b.getClass().hashCode();
    }

    @Test
    public void test() {
        System.out.println(ComparatorUtil.compare(1, 1L));
        System.out.println(ComparatorUtil.compare("a", "b"));
        System.out.println(ComparatorUtil.compare("a", 1));
        System.out.println(ComparatorUtil.compare(1, "a"));
        System.out.println(ComparatorUtil.compare(1, 1.0));
        System.out.println(ComparatorUtil.compare(null, null));
        System.out.println(ComparatorUtil.compare(null, -1));
        System.out.println(ComparatorUtil.compare(1, null));
        System.out.println(ComparatorUtil.compare("aa", "ab"));
    }

    @Test
    public void testSpeed() {
        for (int i = 2; i < 10_000; ) {
            i *= 2;
            long[] tss = Timeit.ofActions()
                    .addAction(() -> {
                        int ignore = ComparatorUtil.compare("a", "b");
                    })
                    .addAction(() -> {
                        int ignore = compareTo("a", "b");
                    })
                    .addAction(() -> {
                        int ignore = compareTo2("a", "b");
                    })
                    .addAction(() -> {
                        int ignore = "a".hashCode() - "b".hashCode();
                    })
                    .repeat(i).count(16).skip(4).run();

            String s = Arrays.stream(tss)
                    .mapToObj(it -> String.format("%6.3fus", it / 1000.))
                    .collect(Collectors.joining("\t"));
            System.out.printf("%4d\t%s\n", i, s);
        }
    }
}
