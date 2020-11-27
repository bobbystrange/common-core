package org.dreamcat.common.core.tree;

import java.util.ArrayList;
import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.tree.AVLHashTree;
import org.dreamcat.common.tree.BinaryNodes;
import org.junit.Test;

/**
 * Create by tuke on 2020/5/29
 */
public class BinaryNodesTest {

    @Test
    public void preOrderTest() {
        AVLHashTree<Integer, Integer> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i * i);
        }

        ArrayList<Integer> list1 = new ArrayList<>();
        tree.preOrder((k, v) -> {
            list1.add(k);
        });
        System.out.println(list1);
        System.out.println("\n");

        ArrayList<Integer> list2 = new ArrayList<>();
        BinaryNodes.preOrder(tree.getRoot(), node -> {
            list2.add(node.key);
        });
        System.out.println(list2);
        System.out.println("\n");
    }

    @Test
    public void preOrderTestSpeed() {
        AVLHashTree<Integer, Integer> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i * i);
        }

        System.out.println("    recurse \t no-recurse");
        for (int i = 1; i < (1 << 16); i *= 2) {
            String line = Timeit.ofActions()
                    .addAction(() -> {
                        tree.preOrder((k, v) -> {
                            // noop
                        });
                    })
                    .addAction(() -> {
                        BinaryNodes.preOrder(tree.getRoot(), (node) -> {
                            // noop
                        });
                    })
                    .count(10).skip(2).repeat(i)
                    .runAndFormatUs();
            System.out.printf("%03d \t %s\n", i, line);
        }

    }

    /// level

    @Test
    public void levelOrderTest() {
        AVLHashTree<Integer, Integer> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i * i);
        }

        ArrayList<Integer> list1 = new ArrayList<>();
        tree.levelOrder((k, v, level) -> {
            list1.add(k);
        });
        System.out.println(list1);
        System.out.println("\n");

        ArrayList<Integer> list2 = new ArrayList<>();
        BinaryNodes.levelOrder(tree.getRoot(), (node, level) -> {
            list2.add(node.key);
        });
        System.out.println(list2);
        System.out.println("\n");
    }

    @Test
    public void levelOrderTestSpeed() {
        AVLHashTree<Integer, Integer> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i * i);
        }

        System.out.println("    recurse \t no-recurse");
        for (int i = 1; i < (1 << 16); i *= 2) {
            String line = Timeit.ofActions()
                    .addAction(() -> {
                        tree.levelOrder((k, v, level) -> {
                            // noop
                        });
                    })
                    .addAction(() -> {
                        BinaryNodes.levelOrder(tree.getRoot(), (node, level) -> {
                            // noop
                        });
                    })
                    .count(10).skip(2).repeat(i)
                    .runAndFormatUs();
            System.out.printf("%03d \t %s\n", i, line);
        }

    }

}
