package org.dreamcat.common.core.tree;

import org.dreamcat.common.core.Timeit;
import org.dreamcat.common.function.ThrowableSupplier;
import org.dreamcat.common.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.dreamcat.common.util.PrintUtil.printf;
import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/4/19
 */
public class AVLHashTreeTest {

    @Test
    public void testPut() {
        AVLHashTree<Integer, String> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i + "");
        }

        List<List<String>> msgs = new ArrayList<>(5);
        tree.levelOrder((k, v, level) -> {
            int size = msgs.size();
            if (size < level) {
                msgs.add(new ArrayList<>());
            }
            msgs.get(level - 1).add(k + "");
        });

        String s = msgs.stream().map(line -> String.join("\t", line)).collect(Collectors.joining("\n"));
        println(s);
    }

    @Test
    public void testRemove() {
        AVLHashTree<Integer, String> tree = new AVLHashTree<>();
        for (int i = 0; i < 32; i++) {
            tree.put(i, i * i + "");
        }

        for (int i = 16; i < 32; i++) {
            tree.remove(i);
        }

        List<List<String>> msgs = new ArrayList<>(5);
        tree.levelOrder((k, v, level) -> {
            int size = msgs.size();
            if (size < level) {
                msgs.add(new ArrayList<>());
            }
            msgs.get(level - 1).add(k + "");
        });

        String s = msgs.stream().map(line -> String.join("\t", line)).collect(Collectors.joining("\n"));
        println(s);
    }

    @Test
    public void testSpeed() {
        println("\t\tHashMap\t\tAVLTree\t\tAVLHashMap\tRBHashMap");
        for (int i = 1; i < (1 << 20); i *= 2) {
            int finalI = i;
            long[] ts = Timeit.ofActions()
                    .addUnaryAction(
                            HashMap::new,
                            map -> {
                                for (int k = 0; k < finalI; k++) {
                                    map.put(k, k);
                                }
                            })
                    .addUnaryAction(
                            (ThrowableSupplier<AVLHashTree<Integer, Integer>>) AVLHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(k, k);
                                }
                            })
                    .addUnaryAction(
                            (ThrowableSupplier<AVLHashMap<Integer, Integer>>) AVLHashMap::new,
                            map -> {
                                for (int k = 0; k < finalI; k++) {
                                    map.put(k, k);
                                }
                            })
                    .addUnaryAction(
                            (ThrowableSupplier<RBHashTree<Integer, Integer>>) RBHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(k, k);
                                }
                            })
                    .count(32).skip(4).run();

            String line = Arrays.stream(ts).mapToObj(it -> String.format("%6.3fus", it / 1000.))
                    .collect(Collectors.joining("\t"));
            printf("%6d\t%s\n", i, line);
        }

    }

    @Test
    public void testSpeedAsc() {
        println("\t\tAVLTree\t\t  RBTree\t\tBTree");
        for (int i = 1; i < (1 << 20); i *= 2) {
            int finalI = i;
            long[] ts = Timeit.ofActions()
                    .addUnaryAction(
                            (ThrowableSupplier<AVLHashTree<Integer, Integer>>) AVLHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(k, k);
                                }
                            })
                    .addUnaryAction(
                            (ThrowableSupplier<RBHashTree<Integer, Integer>>) RBHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(k, k);
                                }
                            })
                    .addUnaryAction(
                            () -> new BTree<>(3, Integer[]::new),
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(k);
                                }
                            })
                    .count(32).skip(4).run();

            String line = Arrays.stream(ts).mapToObj(it -> String.format("%6.3fus", it / 1000.))
                    .collect(Collectors.joining("\t"));
            printf("%6d\t%s\n", i, line);
        }
    }

    @Test
    public void testSpeedRandom() {
        println("\t\tAVLTree\t\t  RBTree\t\tBTree");
        for (int i = 1; i < (1 << 20); i *= 2) {
            int finalI = i;
            long[] ts = Timeit.ofActions()
                    .addUnaryAction(
                            (ThrowableSupplier<AVLHashTree<String, Integer>>) AVLHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(RandomUtil.uuid(), k);
                                }
                            })
                    .addUnaryAction(
                            (ThrowableSupplier<RBHashTree<String, Integer>>) RBHashTree::new,
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(RandomUtil.uuid(), k);
                                }
                            })
                    .addUnaryAction(
                            () -> new BTree<>(3, String[]::new),
                            tree -> {
                                for (int k = 0; k < finalI; k++) {
                                    tree.put(RandomUtil.uuid());

                                }
                                tree.printLevel();
                            })
                    .count(32).skip(4).run();

            String line = Arrays.stream(ts).mapToObj(it -> String.format("%6.3fus", it / 1000.))
                    .collect(Collectors.joining("\t"));
            printf("%6d\t%s\n", i, line);
        }
    }
}