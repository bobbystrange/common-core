package org.dreamcat.common.core;

import lombok.Data;
import org.dreamcat.common.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * A useful class for ORM framework
 * <p>
 * Create by tuke on 2020/8/27
 */
@Data
public class FieldColumn {
    public Field field;
    public List<Annotation> annotations;
    public List<FieldColumn> children;
    // Collection/Array
    public boolean component;

    public static List<FieldColumn> parse(Class<?> clazz) {
        return parse(clazz, ReflectUtil::isFlat);
    }

    public static List<FieldColumn> parse(Class<?> clazz, Predicate<Class<?>> flatPredicate) {
        if (flatPredicate.test(clazz) || ReflectUtil.isCollectionOrArray(clazz)) {
            throw new IllegalArgumentException("flat type is unsupported on class " + clazz);
        }

        List<Field> fields = ReflectUtil.retrieveFields(clazz);
        // inner class or static
        fields.removeIf(it -> it.isSynthetic() || Modifier.isStatic(it.getModifiers()));
        int size = fields.size();
        List<FieldColumn> columns = new ArrayList<>(size);

        for (Field field : fields) {
            FieldColumn column = new FieldColumn();
            columns.add(column);

            Class<?> fieldType = field.getType();
            column.field = field;
            column.annotations = ReflectUtil.retrieveAnnotations(field);
            if (flatPredicate.test(fieldType)) continue;

            Class<?> type = fieldType;
            // fieldType extends Collection
            if (Collection.class.isAssignableFrom(fieldType)) {
                type = ReflectUtil.getTypeArgument(field);
                column.component = true;
            }
            // fieldType extends Object[]
            else if (fieldType.isArray()) {
                type = fieldType.getComponentType();
                column.component = true;
            }
            if (flatPredicate.test(type)) continue;

            column.children = parse(type);
        }
        return columns;
    }

}
