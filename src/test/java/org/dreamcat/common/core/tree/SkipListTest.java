package org.dreamcat.common.core.tree;

import org.junit.Test;

import static org.dreamcat.common.util.ConsoleUtil.printf;
import static org.dreamcat.common.util.ConsoleUtil.println;

/**
 * Create by tuke on 2020/4/4
 */
public class SkipListTest {

    @Test
    public void testProb() {
        for (int i=1; i<=99; i++) {
            float prob = (float)(i * 0.01);
            printf("%.2f\t%d\n", prob, SkipList.maxLevel(prob));
        }
    }

    @Test
    public void test() {
        SkipList<Integer, Integer> list = new SkipList<>(0.25);
        for (int i=0; i<100; i++){
            list.put(i, i);
        }

        println(list.prettyToString(2));

        println("size", list.size());
        for (int i=0; i<100; i++){
            println(i, list.get(i));
        }
    }
}
