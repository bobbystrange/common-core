package org.dreamcat.common.core.tree;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.dreamcat.common.util.ConsoleUtil.println;

/**
 * Create by tuke on 2020/4/5
 */
public class AVLTreeTest {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testHashMap() {
        Object v;
        HashMap m = new HashMap();
        v = m.put(null, 1);
        v = v = m.get(null);
        v = m.put(0, 2);
        v = m.get(0);
        v = m.put("", 3);
        v = m.get("");
    }

    @Test
    public void testTree() {
        AvlTree<String, String> tree = new AvlTree<>();
        println("put B", tree.put("B", "John"));
        println("put e",tree.put("e", "Lennon"));
        println("put a",tree.put("a", "Paul"));
        println("put a",tree.put("a", "Paul"));
        println();

        tree.forEach((k, v) -> {
            println(k, "->", v);
        });
        println();

        println("B", tree.get("B"));
        println("e", tree.get("e"));
        println("a", tree.get("a"));
        println();

        println("remove B", tree.remove("B"));
        println("remove a", tree.remove("a"));
        println("remove a", tree.remove("a"));

        tree.forEach((k, v) -> {
            println(k, "->", v);
        });
        println();
    }
}
