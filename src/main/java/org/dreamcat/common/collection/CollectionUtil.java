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
        HashMap<K, V> map = new HashMap<>();
        if (ObjectUtil.isEmpty(input)) return map;

        int size = input.length;
        ObjectUtil.requireOdd(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Object... input) {
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
        if (ObjectUtil.isEmpty(input)) return map;

        int size = input.length;
        ObjectUtil.requireEven(size, "input.length");
        for (int i = 0; i < size; i += 2) {
            map.put((K) input[i], (V) input[i + 1]);
        }
        return map;
    }


}
