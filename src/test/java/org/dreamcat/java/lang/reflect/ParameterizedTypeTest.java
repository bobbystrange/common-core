package org.dreamcat.java.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import lombok.Data;
import org.junit.Test;

/**
 * Create by tuke on 2020/8/17
 */
@Data
public class ParameterizedTypeTest<T, R> {

    T left;
    R right;

    public ParameterizedTypeTest() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] types = parameterizedType.getActualTypeArguments();
            System.out.println(Arrays.toString(types));
        }
    }

    @Test
    public void test() {
        ParameterizedTypeTest<String, Double> bean = new ParameterizedTypeTest<>();
        bean.left = "";
        bean.right = 1.0;
        System.out.println(bean);
    }

}
