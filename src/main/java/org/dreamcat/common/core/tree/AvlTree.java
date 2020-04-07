package org.dreamcat.common.core.tree;

import lombok.AllArgsConstructor;
import org.dreamcat.common.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.dreamcat.common.core.tree.AvlNode.*;

/**
 * Create by tuke on 2020/4/5
 */
public class AvlTree<K, V> implements Map<K, V> {
    AvlNode<Entry<K, V>> root;
    int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (isEmpty()) return false;
        for (AvlNode<Entry<K, V>> node : root) {
            if (Objects.equals(node.item.value, value)) return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        AvlNode<Entry<K, V>> node = select(root, hash(key));
        return node != null ? node.item.value : null;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        WriteResult<Entry<K, V>> result = result();
        root = insert(root, hash, entry(key, value), result);
        return result.item == null ? null : result.item.value;
    }

    @Override
    public V remove(Object key) {
        int hash = hash(key);
        WriteResult<Entry<K, V>> result = result();
        delete(root, hash, result);
        return result.item == null ? null : result.item.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (ObjectUtil.isEmpty(m)) return;
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        drop(root);
        root = null;
        size = 0;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v = get(key);
        return v != null ? v : defaultValue;
    }


    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        inOrder(root, (node) -> {
            keys.add(node.item.key);
        });
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> vals = new ArrayList<>();
        inOrder(root, (node) -> {
            vals.add(node.item.value);
        });
        return vals;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        inOrder(root, (node) -> {
            entries.add(node.item);
        });
        return entries;
    }


    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        inOrder(root, (node) -> {
            action.accept(node.item.key, node.item.value);
        });
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        inOrder(root, (node) -> {
            node.item.value = function.apply(node.item.key, node.item.value);
        });
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public V replace(K key, V value) {
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @AllArgsConstructor
    static class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Map.Entry)) return false;

            Map.Entry entry = (Map.Entry) obj;
            return Objects.equals(key, entry.getKey()) &&
                    Objects.equals(value, entry.getValue());
        }
    }

    /// static utils

    static <K, V> Entry<K, V> entry(K key, V value) {
        return new Entry<>(key, value);
    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /// override node methods

    private <E> AvlNode<E> insert(AvlNode<E> node, int hash, E item, WriteResult<E> result) {
        if (node == null) {
            node = new AvlNode<>();
            node.hash = hash;
            node.item = item;
            return node;
        }

        // override it
        if (hash == node.hash) {
            result.item = node.item;
            node.item = item;
            size++;
            return node;
        }

        if (hash > node.hash) {
            node.right = insert(node.right, hash, item, result);
        } else {
            node.left = insert(node.left, hash, item, result);
        }
        return AvlRotation.balance(node);
    }

    private <E> AvlNode<E> delete(AvlNode<E> node, int hash, WriteResult<E> result) {
        if (node == null) return null;

        if (hash == node.hash) {
            result.item = node.item;
            size--;
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
            node.right = delete(node.right, hash, result);
        } else {
            node.left = delete(node.left, hash, result);
        }

        return AvlRotation.balance(node);
    }

    private static class WriteResult<E> {
        E item;
    }

    private WriteResult<Entry<K, V>> result() {
        return new WriteResult<>();
    }

}
