package com.tukeof.common.util.collection;

import com.tukeof.common.annotation.NotNull;
import com.tukeof.common.annotation.Nullable;
import com.tukeof.common.util.ReflectUtil;
import com.tukeof.common.util.bean.BeanStringUtil;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Create by tuke on 2019-02-14
 */
public class CollectionUnionUtil {

    // keyRowGetter
    // ReflectUtil::getFirstFieldValue
    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    // blockColumnFormatter
    // (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            @NotNull List<T>[] blocks) {
        return unionVertical(
                ReflectUtil::getFirstFieldValue,
                excludeAnnotations,
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    // blockColumnFormatter
    // (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            @NotNull Function<T, K> keyRowGetter,
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            @NotNull List<T>[] blocks) {
        return unionVertical(
                keyRowGetter,
                (K key) -> Collections.singletonList(key.toString()),
                excludeAnnotations,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // blockColumnFormatter
    // (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            @NotNull Function<T, K> keyRowGetter,
            @NotNull Function<K, List<String>> keyColumnFormatter,
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            @NotNull List<T>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                keyRowGetter,
                keyColumnFormatter,
                (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations),
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K, T> List<List<String>> unionVertical(
            @NotNull Function<T, K> keyRowGetter,
            @NotNull Function<T, List<String>> blockColumnFormatter,
            int blockWidth,
            @NotNull List<T>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                keyRowGetter,
                (K key) -> Collections.singletonList(key.toString()),
                blockColumnFormatter,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K> List<List<String>> unionVertical(
            @NotNull Function<Object, K>[] keyRowGetters,
            @NotNull Function<K, List<String>> keyColumnFormatter,
            @NotNull Function<Object, List<String>>[] blockColumnFormatters,
            int[] blockWidths,
            @NotNull List<Object>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                keyRowGetters,
                (K key) -> Collections.singletonList(key.toString()),
                blockColumnFormatters,
                blockWidths,
                blocks);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    // blockColumnFormatter
    // (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            @NotNull Set<K> keys,
            @Nullable int blockWidth,
            @NotNull Map<K, ? extends T>[] blocks) {
        return unionVertical(
                (K key) -> Collections.singletonList(key.toString()),
                excludeAnnotations,
                keys,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // blockColumnFormatter
    // (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            @NotNull Function<K, List<String>> keyColumnFormatter,
            @Nullable Class<? extends Annotation>[] excludeAnnotations,
            @NotNull Set<K> keys,
            @Nullable int blockWidth,
            @NotNull Map<K, ? extends T>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                keyColumnFormatter,
                (T bean) -> BeanStringUtil.toStringList(bean, excludeAnnotations),
                keys,
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K, T> List<List<String>> unionVertical(
            @NotNull Function<T, List<String>> blockColumnsFormatter,
            @NotNull Set<K> keys,
            @Nullable int blockWidth,
            @NotNull Map<K, ? extends T>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                (K key) -> Collections.singletonList(key.toString()),
                blockColumnsFormatter,
                keys,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K> List<List<String>> unionVertical(
            @NotNull Function<Object, List<String>>[] blockColumnsFormatters,
            @NotNull Set<K> keys,
            @Nullable int[] blockWidths,
            @NotNull Map<K, ?>[] blocks) {
        return BasicCollectionUnionUtil.unionVertical(
                (K key) -> Collections.singletonList(key.toString()),
                blockColumnsFormatters,
                keys,
                blockWidths,
                blocks);
    }

}
