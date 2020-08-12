package org.dreamcat.common.collection;

import org.dreamcat.common.util.ObjectUtil;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by tuke on 2020/4/24
 */
public class CollectionUtil {

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> newHashMap(Object... input) {
        if (ObjectUtil.isEmpty(input)) return new HashMap<>();

        int size = input.length;
        HashMap<K, V> map = new HashMap<>(size);
        ObjectUtil.requireOdd(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Object... input) {
        if (ObjectUtil.isEmpty(input)) return new ConcurrentHashMap<>();

        int size = input.length;
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>(size);
        ObjectUtil.requireEven(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }


}
