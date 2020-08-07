package org.dreamcat.common.core.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.dreamcat.common.util.FormatUtil.println;

/**
 * Create by tuke on 2020/4/19
 */
public class RBHashTreeTest {

    @Test
    public void testPut() {
        RBHashTree<Integer, String> tree = new RBHashTree<>();
        for (int i = 0; i < 1000; i++) {
            tree.put(i, null);
        }

        List<List<String>> msgs = new ArrayList<>(5);
        tree.levelOrder((k, v, red, level) -> {
            int size = msgs.size();
            if (size < level) {
                msgs.add(new ArrayList<>());
            }
            msgs.get(level - 1).add(k + "(" + (red ? "R" : "B") + ")");
        });

        String s = msgs.stream().map(line -> String.join("\t", line)).collect(Collectors.joining("\n"));
        println(s);
    }

}
