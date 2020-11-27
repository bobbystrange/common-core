package org.dreamcat.common.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Create by tuke on 2020/4/18
 */
@SuppressWarnings("unchecked")
public abstract class BinaryNode<Node extends BinaryNode<Node>> implements Iterable<Node> {

    protected Node left;
    protected Node right;

    /// for-each methods

    public void preOrder(Consumer<? super Node> action) {
        action.accept((Node) this);
        if (left != null) {
            left.preOrder(action);
        }
        if (right != null) {
            right.preOrder(action);
        }
    }

    public void inOrder(Consumer<? super Node> action) {
        if (left != null) {
            left.inOrder(action);
        }
        action.accept((Node) this);
        if (right != null) {
            right.inOrder(action);
        }
    }

    public void postOrder(Consumer<? super Node> action) {
        if (left != null) {
            left.postOrder(action);
        }
        if (right != null) {
            right.postOrder(action);
        }
        action.accept((Node) this);
    }

    public void levelOrder(BiConsumer<? super Node, Integer> action) {
        BinaryNodes.levelOrder((Node) this, action);
    }

    // level order iterator

    @Override
    public Iterator<Node> iterator() {
        return new Iter<>((Node) this);
    }

    protected static class Iter<Node extends BinaryNode<Node>> implements Iterator<Node> {

        private final LinkedList<Node> levelNodes;

        Iter(Node node) {
            levelNodes = new LinkedList<>();
            if (node != null) {
                levelNodes.addLast(node);
            }
        }

        @Override
        public boolean hasNext() {
            return !levelNodes.isEmpty();
        }

        @Override
        public Node next() {
            Node node = levelNodes.removeFirst();
            if (node.left != null) levelNodes.addLast(node.left);
            if (node.right != null) levelNodes.addLast(node.right);
            return node;
        }
    }

}
