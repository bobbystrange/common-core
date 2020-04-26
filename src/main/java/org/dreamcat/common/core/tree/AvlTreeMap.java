package org.dreamcat.common.core.tree;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Objects;

/**
 * Create by tuke on 2020/4/5
 */
public abstract class AvlTreeMap<K, V> implements Map<K, V> {

    private Node<K, V>[] table;

    @AllArgsConstructor
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        K key;
        V value;
        Node<K, V> next;

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

    static class TreeNode<K, V> extends Node<K, V> {


        TreeNode(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }

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
}
