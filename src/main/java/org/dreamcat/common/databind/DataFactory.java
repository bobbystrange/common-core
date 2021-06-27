package org.dreamcat.common.databind;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import org.dreamcat.common.util.ReflectUtil;

/**
 * @author Jerry Will
 * @since 2021-06-20
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataFactory {

    public static Object from(Object src, DataType type) {
        return null;
    }

    public static Object fromString(String src, DataType type) {
        return null;
    }

    /**
     * map2map, or map2bean
     *
     * @param src  source map
     * @param type data type, one of Map or Pojo
     * @return instance of type
     */
    public static Object fromMap(Map<String, ?> src, DataType type) {
        Object instance = type.newInstance();
        Class<?> clazz = type.getType();
        DataType[] parameterTypes = type.getParameterTypes();

        boolean isMap = Map.class.isAssignableFrom(clazz);
        if (ReflectUtil.isFlat(clazz) && !isMap) return instance;

        Map<String, Field> fieldMap = null;
        for (Entry<String, ?> entry : src.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();

            if (isMap) {
                Object key = fromString(k, parameterTypes[0]);
                Object value = from(v, parameterTypes[1]);
                ((Map) instance).put(key, value);
            } else {
                if (fieldMap == null) {
                    fieldMap = ReflectUtil.retrieveFieldMap(clazz);
                }
                Field field = fieldMap.get(k);
                if (field == null) continue;

                ReflectUtil.setValue(instance, field, null);
            }
        }
        return instance;
    }
}
