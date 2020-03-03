package org.dreamcat.common.collection;

import org.dreamcat.common.bean.BeanArrayUtil;
import org.dreamcat.common.util.ReflectUtil;

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
    // (T bean) -> BeanUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            List<T>[] blocks) {
        return unionVertical(
                ReflectUtil::getFirstFieldValue,
                excludeAnnotations,
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    // blockColumnFormatter
    // (T bean) -> BeanUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            Function<T, K> keyRowGetter,
            Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            List<T>[] blocks) {
        return unionVertical(
                keyRowGetter,
                (K key) -> Collections.singletonList(key.toString()),
                excludeAnnotations,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // blockColumnFormatter
    // (T bean) -> BeanUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            Function<T, K> keyRowGetter,
            Function<K, List<String>> keyColumnFormatter,
            Class<? extends Annotation>[] excludeAnnotations,
            int blockWidth,
            List<T>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
                keyRowGetter,
                keyColumnFormatter,
                (T bean) -> BeanArrayUtil.toStringList(bean, excludeAnnotations),
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K, T> List<List<String>> unionVertical(
            Function<T, K> keyRowGetter,
            Function<T, List<String>> blockColumnFormatter,
            int blockWidth,
            List<T>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
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
            Function<Object, K>[] keyRowGetters,
            Function<K, List<String>> keyColumnFormatter,
            Function<Object, List<String>>[] blockColumnFormatters,
            int[] blockWidths,
            List<Object>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
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
    // (T bean) -> BeanUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            Class<? extends Annotation>[] excludeAnnotations,
            Set<K> keys,
            int blockWidth,
            Map<K, ? extends T>[] blocks) {
        return unionVertical(
                (K key) -> Collections.singletonList(key.toString()),
                excludeAnnotations,
                keys,
                blockWidth,
                blocks);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    // blockColumnFormatter
    // (T bean) -> BeanUtil.toStringList(bean, excludeAnnotations)
    public static <K, T> List<List<String>> unionVertical(
            Function<K, List<String>> keyColumnFormatter,
            Class<? extends Annotation>[] excludeAnnotations,
            Set<K> keys,
            int blockWidth,
            Map<K, ? extends T>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
                keyColumnFormatter,
                (T bean) -> BeanArrayUtil.toStringList(bean, excludeAnnotations),
                keys,
                blockWidth,
                blocks);
    }

    // keyColumnFormatter
    // (K key) -> Collections.singletonList(key.toString())
    public static <K, T> List<List<String>> unionVertical(
            Function<T, List<String>> blockColumnsFormatter,
            Set<K> keys,
            int blockWidth,
            Map<K, ? extends T>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
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
            Function<Object, List<String>>[] blockColumnsFormatters,
            Set<K> keys,
            int[] blockWidths,
            Map<K, ?>[] blocks) {
        return CollectionUnionBasicUtil.unionVertical(
                (K key) -> Collections.singletonList(key.toString()),
                blockColumnsFormatters,
                keys,
                blockWidths,
                blocks);
    }

}
