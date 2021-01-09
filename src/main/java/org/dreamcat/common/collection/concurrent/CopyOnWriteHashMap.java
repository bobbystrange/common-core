package org.dreamcat.common.collection.concurrent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Create by tuke on 2021/1/9
 *
 * @see java.util.concurrent.CopyOnWriteArrayList
 */
public class CopyOnWriteHashMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    final transient ReentrantLock lock = new ReentrantLock();

    private volatile Map<K, V> map;

    public CopyOnWriteHashMap() {
        setMap(new HashMap<>(0));
    }

    public CopyOnWriteHashMap(Map<K, V> map) {
        Map<K, V> elements;
        if (map.getClass() == CopyOnWriteHashMap.class)
            elements = ((CopyOnWriteHashMap<K, V>) map).getMap();
        else {
            elements = new HashMap<>(map);
        }
        setMap(elements);
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getMap().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return getMap().get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Map<K, V> elements = getMap();
            V oldValue = elements.get(key);

            if (oldValue != value) {
                Map<K, V> newElements = (Map<K, V>) ((HashMap<K, V>) elements).clone();
                newElements.put(key, value);
                setMap(newElements);
            } else {
                // Not quite a no-op; ensures volatile write semantics
                setMap(elements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return getMap().remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            getMap().putAll(m);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            getMap().clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return getMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return getMap().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return getMap().entrySet();
    }

    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            CopyOnWriteHashMap<K, V> clone =
                    (CopyOnWriteHashMap<K, V>) super.clone();
            clone.resetLock();
            return clone;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    final Map<K, V> getMap() {
        return map;
    }

    final void setMap(Map<K, V> map) {
        this.map = map;
    }

    // Support for resetting lock while deserializing
    private void resetLock() {
        UNSAFE.putObjectVolatile(this, lockOffset, new ReentrantLock());
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long lockOffset;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = CopyOnWriteHashMap.class;
            lockOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("lock"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
