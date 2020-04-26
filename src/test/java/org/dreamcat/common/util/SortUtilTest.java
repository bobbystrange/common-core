package org.dreamcat.common.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/4/4
 */
@Slf4j
public class SortUtilTest {

    private static ScoreInteger of(int value) {
        return new ScoreInteger(value);
    }

    @Test
    public void testSelectSort() {
        int[] a = new int[]{1, 6, 3, 7, 9, 2, 4, 3};
        SortUtil.insertSort(a);
        log.info("{}", Arrays.toString(a));

        int[] b = new int[]{1, 2, 3, 3, 4, 6, 7, 9};
        SortUtil.bubbleSort(b);
        log.info("{}", Arrays.toString(b));
    }

    @Test
    public void testBinSort() {
        LinkedList<ScoreInteger> list = new LinkedList<>();
        for (int i = 0; i <= 100; i++) {
            list.add(of(i));
        }
        log.info("{}", list.toString());
        SortUtil.binSort(list, ScoreInteger::score, 10);
        log.info("{}", list.toString());
        SortUtil.bubbleSort(list);
        log.info("{}", list.toString());
        SortUtil.binSort(list, ScoreInteger::score, 10);
        log.info("{}", list.toString());
        SortUtil.selectSort(list);
        log.info("{}", list.toString());
        SortUtil.binSort(list, ScoreInteger::score, 10);
        log.info("{}", list.toString());
        SortUtil.insertSort(list);
        log.info("{}", list.toString());
    }

    @Test
    public void testRadixSort() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            int v = RandomUtil.randi(1_000_000);
            list.add(v);
        }

        println(list);
        SortUtil.radixSort(list, (e, i) -> {
            // i = 0,1,2
            int ind = i * 2;
            return NumericUtil.digitAt(e, ind + 1) * 10 + NumericUtil.digitAt(e, ind);
        }, 100, 3);
        println(list);
    }

    @AllArgsConstructor
    private static class ScoreInteger implements Comparable<ScoreInteger> {
        private int value;

        @Override
        public int compareTo(ScoreInteger o) {
            return value - o.value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public int score() {
            return value % 10;
        }
    }
}
