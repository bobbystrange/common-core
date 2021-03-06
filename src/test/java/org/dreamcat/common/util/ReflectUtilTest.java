package org.dreamcat.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.bean.BeanFormatUtil;
import org.dreamcat.test.BeanData;
import org.junit.Test;

/**
 * Create by tuke on 2019-02-12
 */
@Slf4j
public class ReflectUtilTest {

    @Test
    public void retrieveSuperClasses() {
        ReflectUtil.retrieveSuperClasses(C.class).forEach(System.out::println);
    }

    @Test
    public void retrieveSubClasses() {
        ReflectUtil.retrieveSubClasses(A.class).forEach(System.out::println);
    }

    @Test
    public void hasAnnotation() throws NoSuchFieldException {
        BeanData.Pojo obj = BeanData.ofPojo();
        Field nameField = obj.getClass().getDeclaredField("name");

        assert ReflectUtil.hasAnyAnnotation(nameField, BeanData.Ann.class);
    }

    @Test
    public void anyAssignable() {
        assert ReflectUtil.hasAnySuperClass(ArrayList.class, List.class, int.class, Date.class);
        assert ReflectUtil
                .hasAnySuperClass(ArrayList.class, String.class, Collection.class, Iterable.class);
    }

    @Test
    public void getParameterNames() {
        Method[] methods = ReflectUtil.class.getDeclaredMethods();
        for (int i = 0, len = methods.length; i < len; i++) {
            Method method = methods[i];
            log.info("{}, {}, {}", i, method.getName(),
                    Arrays.deepToString(ReflectUtil.getParameterNames(method)));
        }
    }

    @Test
    public void getTypeArguments() throws Exception {
        List<Method> methods = Arrays.asList(
                ReflectUtil.class.getDeclaredMethod("retrieveSuperClasses", Class.class),
                ReflectUtil.class.getDeclaredMethod("getTypeArguments", Field.class),
                ReflectUtil.class.getDeclaredMethod("getParameterNames", Method.class)
        );
        for (Method method : methods) {
            Class<?>[] args = ReflectUtil.getTypeArguments(method);
            log.info("\n{}", method);
            for (int i = 0; i < args.length; i++) {
                log.info("{}:\n{}", i, args[i]);
            }
        }
    }

    @Test
    public void retrieveFields() {
        List<Field> fields = ReflectUtil.retrieveFields(C.class);
        fields.forEach(System.out::println);

        System.out.println("\ngetDeclaredFields");
        Arrays.stream(C.class.getDeclaredFields()).forEach(System.out::println);
        System.out.println("\ngetFields");
        Arrays.stream(C.class.getFields()).forEach(System.out::println);

    }

    @Test
    public void nullify() {
        BeanData.Pojo obj = BeanData.ofPojo();
        ReflectUtil.nullify(obj);
        System.out.println(BeanFormatUtil.pretty(obj));
    }

    private static class A {

        int a;
        String a2;
        static long a3;
    }

    private static class B extends A {

        int b;
        Date b2;
        static String b3;
    }

    private static class C extends B {

        int c;
        Long a2;
        static double b3;
    }

}
