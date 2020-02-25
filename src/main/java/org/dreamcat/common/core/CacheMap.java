package org.dreamcat.common.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dreamcat.common.annotation.Nullable;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2018-12-17
 */
public class CacheMap implements Map<Object, Object> {

    private static final Value NULL_VALUE = new Value(null, 0L);
    private final ConcurrentMap<Object, Value> delegate = new ConcurrentHashMap<>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(Value.valueOf(value));
    }

    @Nullable
    public Object get(Object key) {
        Value value = this.delegate.get(key);
        if (value == null) return null;
        if (value.ttl < System.currentTimeMillis()) {
            this.delegate.remove(key);
            return null;
        }
        return value.value;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Object value = get(key);
        if (value == null) return null;
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException(
                    "cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public Object put(Object key, Object value) {
        return put(key, value, 0L);
    }

    public Object put(Object key, Object value, long ttl) {
        return this.delegate.put(key, Value.valueOf(value, ttl));
    }

    @Override
    public Object remove(Object key) {
        return this.delegate.remove(key);
    }

    public void evict(Object key) {
        this.delegate.remove(key);
    }

    @Override
    public void putAll(Map<?, ?> m) {
        this.delegate.putAll(m.entrySet().stream()
                .collect(Collectors.toMap(k -> k, Value::valueOf)));
    }

    public void clear() {
        this.delegate.clear();
    }

    @Override
    public Set<Object> keySet() {
        return this.delegate.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.delegate.values().stream()
                .map(Value::getValue).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return this.delegate.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().value))
                .collect(Collectors.toSet());
    }

    @Getter
    @AllArgsConstructor
    private static class Value {
        // wrapped value
        private Object value;
        // time to live, millisecond
        private long ttl;

        private static Value valueOf(Object value) {
            return valueOf(value, 0L);
        }

        private static Value valueOf(Object value, long ttl) {
            if (value == null) return NULL_VALUE;
            if (ttl < 0) ttl = 0;
            return new Value(value, ttl);
        }
    }
}
