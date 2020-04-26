package org.dreamcat.common.util;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2019-02-12
 */
@Slf4j
public class ReflectUtilTest<T> {

    @Test
    public void hasAnnotation() throws NoSuchFieldException {
        BeanData.Pojo obj = BeanData.ofPojo();
        Field nameField = obj.getClass().getDeclaredField("name");

        assert ReflectUtil.hasAnyAnnotation(nameField, BeanData.Ann.class);
    }

    @Test
    public void anyAssignable() {
        assert ReflectUtil.hasAnySuperClass(ArrayList.class, List.class, int.class, Date.class);
        assert ReflectUtil.hasAnySuperClass(ArrayList.class, String.class, Collection.class, Iterable.class);
    }

    @Test
    public void getParameterNames() {
        Method[] methods = getClass().getDeclaredMethods();
        for (int i = 0, len = methods.length; i < len; i++) {
            Method method = methods[i];
            log.info("{}, {}, {}", i, method.getName(),
                    Arrays.toString(ReflectUtil.getParameterNames(method)));
        }
    }

    @Test
    public void t() {
        A<String> a = new A<>();
        Type type = ((ParameterizedType) a.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        println(a, type);

        //println(a, a.getTClass());
    }

    static class A<T> {
        public Class<T> getTClass() {
            Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return tClass;
        }
    }
}
