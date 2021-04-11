package org.dreamcat.common.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Create by tuke on 2020/4/24
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    @SafeVarargs
    public static <E> Set<E> setOf(E... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Object... input) {
        if (ObjectUtil.isEmpty(input)) return new HashMap<>();

        int size = input.length;
        HashMap<K, V> map = new HashMap<>(size);
        ObjectUtil.requireEven(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> concurrentMapOf(Object... input) {
        if (ObjectUtil.isEmpty(input)) return new ConcurrentHashMap<>();

        int size = input.length;
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>(size);
        ObjectUtil.requireEven(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T> T firstElement(List<T> o) {
        return elementAt(o, 0);
    }

    public static <T> T firstElement(T[] o) {
        return elementAt(o, 0);
    }

    public static <T> T lastElement(List<T> o) {
        if (ObjectUtil.isEmpty(o)) return null;
        return o.get(o.size() - 1);
    }

    public static <T> T lastElement(T[] o) {
        if (ObjectUtil.isEmpty(o)) return null;
        return o[o.length - 1];
    }

    public static <T> T elementAt(List<T> o, int index) {
        if (ObjectUtil.isEmpty(o)) return null;
        if (index < 0 || index >= o.size()) return null;
        return o.get(index);
    }

    public static <T> T elementAt(T[] o, int index) {
        if (ObjectUtil.isEmpty(o)) return null;
        if (index < 0 || index >= o.length) return null;
        return o[index];
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <T, K> void remainFirstDuplicatedKey(List<T> list, Function<T, K> keyGetter) {
        if (ObjectUtil.isEmpty(list) || keyGetter == null) return;
        Set<K> set = new HashSet<>(list.size());
        ListIterator<T> iterator = list.listIterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            K key = keyGetter.apply(element);
            if (set.contains(key)) {
                iterator.remove();
            } else {
                set.add(key);
            }
        }
    }

    /**
     * {@code new ArrayList<T>(list.stream().collect(Collectors.toMap(keyGetter, a -> a, (a, b) -> b)).values()); }
     *
     * @param list      list to remove duplicated elements
     * @param keyGetter key getter
     * @param <T>       element type
     * @param <K>       key type
     */
    public static <T, K> void remainLastDuplicatedKey(List<T> list, Function<T, K> keyGetter) {
        if (ObjectUtil.isEmpty(list) || keyGetter == null) return;
        Set<K> set = new HashSet<>(list.size());
        ListIterator<T> iterator = list.listIterator();
        while (iterator.hasPrevious()) {
            T element = iterator.previous();
            K key = keyGetter.apply(element);
            if (set.contains(key)) {
                iterator.remove();
            } else {
                set.add(key);
            }
        }
    }

}
