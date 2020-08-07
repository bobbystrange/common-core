package org.dreamcat.common.core.tree;

import org.dreamcat.common.core.Pair;

import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Create by tuke on 2020/5/29
 */
public class BinaryNodes {

    public static <Node extends BinaryNode<Node>> void preOrder(Node node, Consumer<? super Node> action) {
        LinkedList<Node> stack = new LinkedList<>();
        Node current = node;
        while (current != null || !stack.isEmpty()) {
            if (current == null) {
                current = stack.removeFirst().right;
                continue;
            }

            action.accept(current);
            stack.addFirst(current);
            current = current.left;
        }
    }

    public static <Node extends BinaryNode<Node>> void levelOrder(Node node, Consumer<? super Node> action) {
        LinkedList<Node> queue = new LinkedList<>();

        queue.addLast(node);

        Node current;
        while (!queue.isEmpty()) {
            current = queue.removeFirst();
            action.accept(current);

            if (current.left != null) {
                queue.addLast(current.left);
            }
            if (current.right != null) {
                queue.addLast(current.right);
            }

        }
    }

    public static <Node extends BinaryNode<Node>> void levelOrder(Node node, BiConsumer<? super Node, Integer> action) {
        LinkedList<Pair<Node, Integer>> queue = new LinkedList<>();

        queue.addLast(new Pair<>(node, 1));

        Node current;
        while (!queue.isEmpty()) {
            Pair<Node, Integer> pair = queue.removeFirst();
            current = pair.first();
            int level = pair.second();
            action.accept(current, level);

            if (current.left != null) {
                queue.addLast(new Pair<>(current.left, level + 1));
            }
            if (current.right != null) {
                queue.addLast(new Pair<>(current.right, level + 1));
            }
        }
    }

}
