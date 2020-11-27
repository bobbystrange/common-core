package org.dreamcat.common.core.tree;

import org.dreamcat.common.tree.skip.SkipList;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/4
 */
public class SkipListTest {

    @Test
    public void testProb() {
        for (int i = 1; i <= 99; i++) {
            float prob = (float) (i * 0.01);
            System.out.printf("%.2f\t%d\n", prob, SkipList.maxLevel(prob));
        }
    }

    @Test
    public void test() {
        SkipList<Integer, Integer> list = new SkipList<>(0.25);
        for (int i = 0; i < 100; i++) {
            list.put(i, i);
        }

        System.out.println(list.prettyToString(2));

        System.out.println("size" + list.size());
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " " + list.get(i));
        }
    }
}
