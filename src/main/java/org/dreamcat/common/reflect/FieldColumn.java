package org.dreamcat.common.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.dreamcat.common.util.ReflectUtil;

/**
 * A useful class for ORM framework
 * <p>
 * Create by tuke on 2020/8/27
 */
@Data
public class FieldColumn {

    public Field field;
    public List<Annotation> annotations;
    // null if field has a flat type or field is collection-like with a flat element type
    public List<FieldColumn> children;
    // collection-like: Collection/Array
    public boolean collectionLike;

    public static List<FieldColumn> parse(Class<?> clazz) {
        if (ReflectUtil.isFlat(clazz) || ReflectUtil.isCollectionOrArray(clazz)) {
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
            column.annotations = new ArrayList<>(Arrays.asList(field.getDeclaredAnnotations()));
            if (ReflectUtil.isFlat(fieldType)) continue;

            Class<?> type = fieldType;
            // fieldType extends Collection
            if (Collection.class.isAssignableFrom(fieldType)) {
                type = ReflectUtil.getTypeArgument(field);
                column.collectionLike = true;
            }
            // fieldType extends Object[]
            else if (fieldType.isArray()) {
                type = fieldType.getComponentType();
                column.collectionLike = true;
            }
            // type is never null
            if (ReflectUtil.isFlat(type)) continue;

            column.children = parse(type);
        }
        return columns;
    }

}
