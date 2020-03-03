package org.dreamcat.common.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Create by tuke on 2019-02-14
 */
public class CollectionUnionBasicUtil {

    @SuppressWarnings("unchecked")
    public static <K, T> List<List<String>> unionVertical(
            Function<T, K> keyRowGetter,
            Function<K, List<String>> keyColumnFormatter,
            Function<T, List<String>> blockColumnFormatter,
            int blockWidth,
            List<T>[] blocks) {
        int size = blocks.length;
        Map<K, ? extends T>[] blockMaps = new Map[size];
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < size; i++) {
            List<T> block = blocks[i];
            blockMaps[i] = block.stream().collect(Collectors.toMap(
                    it -> {
                        K key = keyRowGetter.apply(it);
                        keys.add(key);
                        return key;
                    },
                    it -> it
            ));
        }
        return unionVertical(
                keyColumnFormatter,
                blockColumnFormatter,
                keys,
                blockWidth,
                blockMaps);
    }

    @SuppressWarnings("unchecked")
    public static <K> List<List<String>> unionVertical(
            Function<Object, K>[] keyRowGetters,
            Function<K, List<String>> keyColumnFormatter,
            Function<Object, List<String>>[] blockColumnFormatters,
            int[] blockWidths,
            List<Object>[] blocks) {
        int size = blocks.length;
        Map<K, ?>[] blockMaps = new Map[size];
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < size; i++) {
            List<Object> block = blocks[i];
            final int j = i;
            blockMaps[i] = block.stream().collect(Collectors.toMap(
                    it -> {
                        K key = keyRowGetters[j].apply(it);
                        keys.add(key);
                        return key;
                    },
                    it -> it
            ));
        }
        return unionVertical(
                keyColumnFormatter,
                blockColumnFormatters,
                keys,
                blockWidths,
                blockMaps);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * vertical union
     *
     * @param keyColumnFormatter   create common fileds by the id object
     * @param blockColumnFormatter every block has a common formatter
     * @param keys                 key object list
     * @param blockWidth           if blockWidth lte 0, then ignore it;
     *                             otherwise assert it equal the block-formatted list's size
     * @param blocks               vertical blocks to fill the matrix
     * @param <K>                  class of the id object
     * @param <T>                  class of the block object
     * @return unioned matrix
     * @throws NullPointerException if any param is null
     */
    public static <K, T> List<List<String>> unionVertical(
            Function<K, List<String>> keyColumnFormatter,
            Function<T, List<String>> blockColumnFormatter,
            Set<K> keys,
            int blockWidth,
            Map<K, ? extends T>[] blocks) {
        List<List<String>> matrix = new ArrayList<>();

        int len = blocks.length;
        int[] widths = new int[blocks.length];
        for (K key : keys) {
            List<String> list = new ArrayList<>(
                    keyColumnFormatter.apply(key));
            for (int i = 0; i < len; i++) {
                T value = blocks[i].get(key);
                if (value == null) {
                    if (widths[i] == 0) {
                        if (blockWidth <= 0)
                            throw new IllegalArgumentException("failed to fill empty string to the matrix, key=" + key);
                        else widths[i] = blockWidth;
                    }
                    for (int j = 0; j < widths[i]; j++) {
                        list.add("");
                    }
                    continue;
                }

                List<String> blockList = blockColumnFormatter.apply(value);
                fillWidthsOrThrow(widths, i, blockList.size());
                list.addAll(blockList);
            }
            matrix.add(list);
        }
        return matrix;
    }

    /**
     * vertical union
     *
     * @param keyColumnFormatter    create common fields by the key object
     * @param blockColumnFormatters every block has its own formatter
     * @param keys                  key object list
     * @param blockWidths           if blockWidth eq null, then ignore it
     * @param blocks                collections
     * @param <K>                   class of the key object, use as a key of map
     * @return unioned matrix
     * @throws NullPointerException           if any param is null
     * @throws IllegalArgumentException       if size of block-columns is not constant when format same-type block element
     * @throws ArrayIndexOutOfBoundsException if any collection or map param's size is not equal
     */
    public static <K> List<List<String>> unionVertical(
            Function<K, List<String>> keyColumnFormatter,
            Function<Object, List<String>>[] blockColumnFormatters,
            Set<K> keys,
            int[] blockWidths,
            Map<K, ?>[] blocks) {
        List<List<String>> matrix = new ArrayList<>();

        int len = blocks.length;
        int[] widths = new int[blocks.length];
        for (K key : keys) {
            List<String> list = new ArrayList<>(
                    keyColumnFormatter.apply(key));
            for (int i = 0; i < len; i++) {
                Object value = blocks[i].get(key);
                if (value == null) {
                    if (widths[i] == 0) {
                        if (blockWidths == null)
                            throw new IllegalArgumentException("failed to fill empty string to the matrix, key=" + key);
                        else widths[i] = blockWidths[i];
                    }
                    for (int j = 0; j < widths[i]; j++) {
                        list.add("");
                    }
                    continue;
                }

                List<String> blockList = blockColumnFormatters[i].apply(value);
                fillWidthsOrThrow(widths, i, blockList.size());
                list.addAll(blockList);
            }
            matrix.add(list);
        }
        return matrix;
    }

    private static void fillWidthsOrThrow(int[] widths, int i, int size) {
        if (widths[i] == 0) {
            widths[i] = size;
        } else {
            if (widths[i] != size) {
                throw new IllegalArgumentException(
                        "expected block-formatted list's size is "
                                + widths[i] + ", but got " + size);
            }
        }
    }

}
