package org.dreamcat.common.text.generate;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dreamcat.common.text.DollarInterpolation;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.common.util.StringUtil;

/**
 * Create by tuke on 2020/11/18
 * <p/>
 * flat a bean to map
 */
public final class PojoFlatGenerator {

    private PojoFlatGenerator() {
    }

    private static final String CONVERT_METHOD_STRING =
            "public static Map<String, Object> flat($class bean) {\n"
                    + "Map<String, Object> map = new HashMap<>();\n"
                    + "$assignment\n"
                    + "return map;\n"
                    + "}";
    private static final String CONVERT_METHOD_GET_OBJECT =
            "$type $variable = $object.$getter();\n";
    private static final String CONVERT_METHOD_PUT_GET =
            "map.put(\"$name\", $object.$getter());\n";

    public static String generate(Class<?> pojoClass) {
        Map<String, String> context = new HashMap<>();
        context.put("class", pojoClass.getSimpleName());
        StringBuilder assignment = new StringBuilder();
        Set<Class<?>> cacheClasses = new HashSet<>(Collections.singleton(pojoClass));
        appendRecursively("bean", pojoClass, assignment, cacheClasses);
        context.put("assignment", assignment.toString());
        return DollarInterpolation.format(CONVERT_METHOD_STRING, context);
    }

    private static void appendRecursively(
            String object, Class<?> pojoClass,
            StringBuilder assignment, Set<Class<?>> cacheClasses) {
        List<Field> fields = ReflectUtil.retrieveNoStaticFields(pojoClass);
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            if (Collection.class.isAssignableFrom(fieldType) ||
                    Map.class.isAssignableFrom(fieldType) ||
                    fieldType.isArray()) continue;

            if (fieldType.isPrimitive() ||
                    String.class.isAssignableFrom(fieldType) ||
                    Enum.class.isAssignableFrom(fieldType) ||
                    Number.class.isAssignableFrom(fieldType) ||
                    Boolean.class.isAssignableFrom(fieldType) ||
                    Temporal.class.isAssignableFrom(fieldType) ||
                    Date.class.isAssignableFrom(fieldType)) {
                Map<String, String> map = new HashMap<>();
                map.put("name", fieldName);
                map.put("object", object);
                map.put("getter", "get" + StringUtil.toCapitalCase(fieldName));
                String putGet = DollarInterpolation.format(CONVERT_METHOD_PUT_GET, map);
                assignment.append(putGet);
            } else {
                if (cacheClasses.contains(fieldType)) continue;

                Set<Class<?>> newCacheClasses = new HashSet<>(cacheClasses);
                newCacheClasses.add(fieldType);

                Map<String, String> map = new HashMap<>();
                map.put("type", fieldType.getSimpleName());
                map.put("variable", fieldName);
                map.put("object", object);
                map.put("getter", "get" + StringUtil.toCapitalCase(fieldName));
                String getObject = DollarInterpolation.format(CONVERT_METHOD_GET_OBJECT, map);
                assignment.append('\n').append(getObject);
                appendRecursively(fieldName, fieldType, assignment, newCacheClasses);
            }
        }
    }
}
