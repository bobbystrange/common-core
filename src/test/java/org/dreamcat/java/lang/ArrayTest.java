package org.dreamcat.java.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import org.dreamcat.common.Timeit;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/21
 */
@Ignore
public class ArrayTest {

    // Note that copy is cheaper than iteration
    @Test
    public void testCopy() {
        for (int i = 2; i < 10_000; i *= 2) {

            int finalI = i;
            String ts = Timeit.ofActions()
                    .addUnaryAction(() -> {
                        ArrayList<Integer> al = new ArrayList<>(finalI);
                        for (int k = 0; k < finalI; k++) {
                            al.add(k);
                        }
                        return al;
                    }, al -> {
                        al.remove(finalI / 2);
                    })
                    .addUnaryAction(() -> {
                        LinkedList<Integer> ll = new LinkedList<>();
                        for (int k = 0; k < finalI; k++) {
                            ll.add(k);
                        }
                        return ll;
                    }, ll -> {
                        ll.remove(finalI / 2);
                    })
                    .repeat(i).count(1).runAndFormatUs();
            System.out.printf("%4d\t%s\n", i, ts);
        }
    }

    @Test
    public void testCopyOf() {
        for (int i = 2; i < 10_000; i *= 2) {
            int[] a = new int[i];
            String ts = Timeit.ofActions()
                    .addAction(() -> {
                        Arrays.copyOf(a, a.length);
                    })
                    .addAction(() -> {
                        int[] b = new int[a.length];
                        System.arraycopy(a, 0, b, 0, a.length);
                    })
                    .repeat(i).count(16).skip(4).runAndFormatUs();
            System.out.printf("%4d\t%s\n", i, ts);
        }
    }
}
