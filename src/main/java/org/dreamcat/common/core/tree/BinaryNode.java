package org.dreamcat.common.core.tree;

import org.dreamcat.common.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
        levelOrder(Collections.singletonList((Node) this), 1, action);
    }

    private void levelOrder(List<Node> levelNodes, int level, BiConsumer<? super Node, Integer> action) {
        if (ObjectUtil.isEmpty(levelNodes)) return;

        List<Node> nextLevelNodes = new ArrayList<>();
        for (Node node : levelNodes) {
            action.accept(node, level);
            if (node.left != null) nextLevelNodes.add(node.left);
            if (node.right != null) nextLevelNodes.add(node.right);
        }
        levelOrder(nextLevelNodes, level + 1, action);
    }

    // level order iterator

    @Override
    public Iterator<Node> iterator() {
        return new Iter<>((Node) this);
    }

    protected static class Iter<Node extends BinaryNode<Node>> implements Iterator<Node> {
        private List<Node> levelNodes;
        private List<Node> nextLevelNodes;
        private int pos;

        Iter(Node node) {
            if (node != null) {
                levelNodes = Collections.singletonList(node);
            } else {
                levelNodes = Collections.emptyList();
            }
            nextLevelNodes = new ArrayList<>();
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return !nextLevelNodes.isEmpty() || pos < levelNodes.size();
        }

        @Override
        public Node next() {
            Node node = levelNodes.get(pos);
            if (node.left != null) nextLevelNodes.add(node.left);
            if (node.right != null) nextLevelNodes.add(node.right);
            if (pos == levelNodes.size() - 1) {
                pos = 0;
                levelNodes = nextLevelNodes;
                nextLevelNodes = new ArrayList<>();
            } else {
                pos++;
            }
            return node;
        }
    }

}
