package org.dreamcat.common.core.component;

import org.dreamcat.common.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Create by tuke on 2018-12-17
 */
public class CacheComponent {

    private final ConcurrentMap<Object, ValueWrapper> store;
    // milli seconds
    private final long defaultTtl;

    public CacheComponent() {
        this(Long.MAX_VALUE);
    }

    public CacheComponent(long defaultTtl) {
        this.store = new ConcurrentHashMap<>();
        this.defaultTtl = defaultTtl;
    }

    public @Nullable
    Object get(Object key) {
        ValueWrapper wrapper = this.store.get(key);
        if (wrapper == null || wrapper.getTtl() < System.currentTimeMillis()) return null;
        return this.store.get(key).getValue();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Object value = get(key);
        if (value == null) return null;
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException(
                    "cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T)value;
    }

    public void put(Object key, Object value) {
        this.put(key, value, this.defaultTtl);
    }

    public void put(Object key, Object value, long ttl) {
        this.store.put(key, ValueWrapper.of(value, ttl));
    }

    public void putIfAbsent(Object key, Object value, long ttl){
        this.store.putIfAbsent(key, ValueWrapper.of(value, ttl));
    }

    public void evict(Object key) {
        this.store.remove(key);
    }

    public synchronized void clear() {
        this.store.clear();
    }

    @Getter
    @AllArgsConstructor
    private static class ValueWrapper {
        private Object value;
        private long ttl;

        static ValueWrapper of(Object value, long ttl){
            if (value == null) value = NULL_VALUE_WRAPPER;
            if (ttl < 0) ttl = 0;
            return new ValueWrapper(value, ttl);
        }
    }

    private static final ValueWrapper NULL_VALUE_WRAPPER = new ValueWrapper(null, 0L);
}
