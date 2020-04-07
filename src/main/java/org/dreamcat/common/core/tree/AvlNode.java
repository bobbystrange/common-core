package org.dreamcat.common.core.tree;

import org.dreamcat.common.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2020/4/5
 */
class AvlNode<E> implements Iterable<AvlNode<E>> {
    int hash;
    E item;
    AvlNode<E> left;
    AvlNode<E> right;
    int height;

    // return new root node
    static <E> AvlNode<E> insert(AvlNode<E> node, int hash, E item) {
        if (node == null) {
            node = new AvlNode<>();
            node.hash = hash;
            node.item = item;
            return node;
        }

        // override it
        if (hash == node.hash) {
            node.item = item;
            return node;
        }

        if (hash > node.hash) {
            node.right = insert(node.right, hash, item);
        } else {
            node.left = insert(node.left, hash, item);
        }
        return AvlRotation.balance(node);
    }

    static <E> AvlNode<E> delete(AvlNode<E> node, int hash) {
        if (node == null) return null;

        if (hash == node.hash) {
            // delete self
            if (node.left != null && node.right != null) {
                // return the leftest node of the right node
                AvlNode<E> right = node.right;
                if (right.left == null) {
                    right.left = node.left;
                    nullify(node);
                    return right;
                } else {
                    AvlNode<E> leftest = popLeftest(right);
                    leftest.left = node.left;
                    leftest.right = right;
                    return leftest;
                }
            } else if (node.left != null) {
                node.item = null;
                return node.left;
            } else {
                node.item = null;
                return node.right;
            }
        } else if (hash > node.hash) {
            node.right = delete(node.right, hash);
        } else {
            node.left = delete(node.left, hash);
        }

        return AvlRotation.balance(node);
    }

    static <E> AvlNode<E> select(AvlNode<E> node, int hash) {
        while (node != null) {
            if (hash == node.hash) {
                return node;
            } else if (hash > node.hash) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    static <E> AvlNode<E> update(AvlNode<E> node, int hash, E item) {
        AvlNode<E> hitNode = select(node, hash);
        if (hitNode != null) {
            hitNode.item = item;
        }
        return hitNode;
    }

    static <E> void drop(AvlNode<E> node) {
        if (node == null) return;
        drop(node.left);
        drop(node.right);

        node.item = null;
        node.left = null;
        node.right = null;
    }

    /// for-each

    static <E> void preOrder(AvlNode<E> node, Consumer<? super AvlNode<E>> action) {
        if (node == null) return;

        action.accept(node);
        preOrder(node.left, action);
        preOrder(node.right, action);
    }

    static <E> void inOrder(AvlNode<E> node, Consumer<? super AvlNode<E>> action) {
        if (node == null) return;

        inOrder(node.left, action);
        action.accept(node);
        inOrder(node.right, action);
    }

    static <E> void postOrder(AvlNode<E> node, Consumer<? super AvlNode<E>> action) {
        if (node == null) return;

        postOrder(node.left, action);
        postOrder(node.right, action);
        action.accept(node);
    }

    static <E> void levelOrder(AvlNode<E> node, int level, BiConsumer<? super AvlNode<E>, Integer> action) {
        levelOrder(Collections.singletonList(node), level, action);
    }

    private static <E> void levelOrder(List<AvlNode<E>> levelNodes, int level, BiConsumer<? super AvlNode<E>, Integer> action) {
        if (ObjectUtil.isEmpty(levelNodes)) return;
        List<AvlNode<E>> nextLevelNodes = new ArrayList<>();
        for (AvlNode<E> i: levelNodes) {
            action.accept(i, level);
            if (i.left != null) nextLevelNodes.add(i.left);
            if (i.right != null) nextLevelNodes.add(i.right);
        }
        levelOrder(nextLevelNodes, level + 1, action);
    }

    @Override
    public Iterator<AvlNode<E>> iterator() {
        return new Iter<>(this);
    }

    /// other methos

    static <E> AvlNode<E> getLeftest(AvlNode<E> node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    static <E> AvlNode<E> popLeftest(AvlNode<E> node) {
        if (node.left.left == null) {
            AvlNode<E> leftest =  node.left;
            node.left = null;
            return leftest;
        }
        return popLeftest(node.left);
    }

    static void nullify(AvlNode<?> node)  {
        node.item = null;
        node.left = null;
        node.right = null;
    }

    ///

    private static class Iter<E> implements Iterator<AvlNode<E>> {
        private List<AvlNode<E>> levelNodes;
        private List<AvlNode<E>> nextLevelNodes;
        private int pos;

        Iter(AvlNode<E> node) {
            levelNodes = new ArrayList<>(Collections.singleton(node));
            nextLevelNodes = new ArrayList<>();
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return nextLevelNodes.isEmpty() && pos == levelNodes.size();
        }

        @Override
        public AvlNode<E> next() {
            AvlNode<E> node = levelNodes.get(pos);
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
