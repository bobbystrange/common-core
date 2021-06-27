package org.dreamcat.common.databind;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;

/**
 * Create by tuke on 2021/4/15
 */
@Slf4j
public class DataType implements Type {

    /**
     * java type
     */
    @Getter
    protected final Class<?> type;

    /**
     * generic type arguments
     */
    @Getter
    protected final DataType[] parameterTypes;

    /**
     * array component type
     */
    @Getter
    protected final DataType componentType;
    /**
     * a immutable empty array and thus can be shared
     */
    @Getter
    protected final Object emptyArray;

    /// transient fields

    /**
     * expressive op, only valid on a pojo type
     * note that lossy generic type since generic erasure
     */
    protected volatile Map<String, DataType> fields;

    public DataType(Class<?> type, DataType... parameterTypes) {
        this.type = type;
        this.parameterTypes = parameterTypes;
        this.componentType = null;
        this.emptyArray = null;
    }

    public DataType(DataType componentType, DataType... parameterTypes) {
        Class<?> componentClass = componentType.getType();
        this.type = ReflectUtil.getArrayClass(componentClass);
        this.parameterTypes = parameterTypes;
        this.componentType = componentType;
        this.emptyArray = Array.newInstance(componentClass, 0);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public boolean isArray() {
        return componentType != null;
    }

    public Object newInstance() {
        if (isArray()) {
            return emptyArray;
        }
        // Collection
        if (type.equals(Collection.class) ||
                type.equals(List.class)) {
            return new ArrayList<>();
        } else if (type.equals(Set.class)) {
            return new HashSet<>();
        } else if (type.equals(Map.class)) {
            return new HashMap<>();
        }

        if (type.isPrimitive()) return ReflectUtil.getZeroValue(type);

        return ReflectUtil.newInstance(type);
    }

    @Override
    public String toString() {
        return this.getSimpleName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, Arrays.hashCode(parameterTypes), componentType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType dataType = (DataType) o;
        return Objects.equals(type, dataType.type) &&
                Arrays.equals(parameterTypes, dataType.parameterTypes) &&
                Objects.equals(componentType, dataType.componentType);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public String getName() {
        if (isArray()) return componentType + "[]";

        String typeName = type.getName();
        if (ObjectUtil.isEmpty(parameterTypes)) return typeName;

        String s = Arrays.stream(parameterTypes).map(Object::toString)
                .collect(Collectors.joining(", "));
        return new StringBuilder(typeName.length() + s.length() + 2)
                .append(typeName).append('<').append(s).append('>').toString();
    }

    public String getSimpleName() {
        if (isArray()) return componentType.getSimpleName() + "[]";

        String typeName = type.getSimpleName();
        if (ObjectUtil.isEmpty(parameterTypes)) return typeName;

        String s = Arrays.stream(parameterTypes).map(DataType::getSimpleName)
                .collect(Collectors.joining(", "));
        return new StringBuilder(typeName.length() + s.length() + 2)
                .append(typeName).append('<').append(s).append('>').toString();
    }

    public Map<String, DataType> loadFields() {
        if (ReflectUtil.isFlat(type)) return null;
        if (fields != null) return fields;

        synchronized (this) {
            if (fields == null) {
                Map<DataType, DataType> cache = new HashMap<>(16);
                cache.put(this, this);
                this.initFields(cache);
            }
        }
        return fields;
    }

    private void initFields(Map<DataType, DataType> cache) {
        Map<String, Field> fieldMap = ReflectUtil.retrieveNoStaticFieldMap(type);
        this.fields = new HashMap<>(fieldMap.size());
        for (Entry<String, Field> entry : fieldMap.entrySet()) {
            String fieldName = entry.getKey();
            Field field = entry.getValue();

            DataType dataType = DataTypes.fromField(field);
            DataType existingDataType = cache.get(dataType);
            if (existingDataType == null) {
                cache.put(dataType, dataType);
                dataType.initFields(cache);
            } else {
                dataType = existingDataType;
            }
            this.fields.put(fieldName, dataType);
        }
    }

}
