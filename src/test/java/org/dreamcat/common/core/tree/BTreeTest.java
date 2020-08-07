package org.dreamcat.common.core.tree;

import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/25
 */
public class BTreeTest {

    @Test
    public void testPut() {
        for (int k = 1; k <= 32; k++) {
            BTree<Integer> tree = new BTree<>(3, Integer[]::new);
            for (int i = 0; i < k; i++) {
                tree.put(i);
            }
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }


    @Test
    public void testRemove() {
        BTree<Integer> tree = new BTree<>(3, Integer[]::new);
        for (int i = 0; i <= 32; i++) {
            tree.put(i);
        }

        for (int i = 0; i <= 32; i++) {
            tree.remove(i);
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }

    @Test
    public void testPutRoot1() {
        int[] data = new int[]{4, 13, 14, 6, 0, 9, 1, 7};
        int order = 3;

        BTree<Integer> tree = new BTree<>(order, Integer[]::new);
        for (int i : data) {
            println("insert\t", i);
            tree.put(i);
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }

    @Test
    public void testPutRoot2() {
        int[] data = new int[]{30, 17, 11, 23, 10, 27, 22, 2, 24, 0, 20, 15, 32, 31,
                9, 21, 3, 126, 29, 25, 16, 8, 19, 7, 13};
        int order = 3;

        BTree<Integer> tree = new BTree<>(order, Integer[]::new);
        for (int i : data) {
            println("insert\t", i);
            tree.put(i);
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }

    @Test
    public void testRemoveRoot() {
        int order = 3;
        int size = 15;

        BTree<Integer> tree = new BTree<>(order, Integer[]::new);
        for (int i = 0; i <= size; i++) {
            tree.put(i);
        }

        tree.printLevel();
        println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        tree.remove(7);
        tree.printLevel();
    }

    @Test
    public void testRandomPut() {
        int order = 3;
        int size = 32;

        BTree<Integer> tree = new BTree<>(order, Integer[]::new);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            list.add(i);
        }

        for (int i = 0; i <= size; i++) {
            int e = RandomUtil.randi(list.size());
            e = list.remove(e);
            println("insert\t", e);
            tree.put(e);
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }

    @Test
    public void testRandomRemove() {
        int order = 3;
        int size = 15;

        BTree<Integer> tree = new BTree<>(order, Integer[]::new);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            tree.put(i);
            list.add(i);
        }

        tree.printLevel();
        println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");

        for (int i = 0; i <= size; i++) {
            int e = RandomUtil.randi(list.size());
            e = list.remove(e);
            println("delete\t", e);
            tree.remove(e);
            tree.printLevel();
            println("==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====");
        }
    }
}
