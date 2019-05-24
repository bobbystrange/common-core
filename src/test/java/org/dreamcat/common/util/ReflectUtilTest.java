package org.dreamcat.common.util;

import org.dreamcat.common.test.BeanBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Create by tuke on 2019-02-12
 */
@Slf4j
public class ReflectUtilTest {

    @Test
    public void hasAnnotation() throws NoSuchFieldException {
        BeanBase obj = BeanBase.newInstance();
        Field nameField = obj.getClass().getDeclaredField("name");

        assert ReflectUtil.hasAnyAnnotation(nameField, BeanBase.Anno.class);
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
}
