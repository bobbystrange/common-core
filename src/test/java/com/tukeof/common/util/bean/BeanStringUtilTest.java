package com.tukeof.common.util.bean;

import com.tukeof.common.test.BeanBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * Create by tuke on 2019-02-16
 */
@Slf4j
public class BeanStringUtilTest {

    private BeanBase obj = BeanBase.newInstance();

    @Test
    public void array() {

        Number[] a = new Number[]{
                0xff,
                2L,
                3D,
                4F,
                0b1010,
        };

        Class<?> clazz = a.getClass();
        log.info("\n{}, \n{}, \n{}, \n{}, \n{}\n",
                clazz, clazz.getName(), clazz.getSimpleName(),
                clazz.getComponentType(),
                clazz.getPackage());
        log.info("\n{}", Arrays.toString(clazz.getClasses()));
        log.info("\n{}", Arrays.toString(clazz.getDeclaredClasses()));

        Object[] o = a;
        String[] types = Arrays.stream(o)
                .map(it -> it.getClass().getName())
                .toArray(String[]::new);
        log.info("\n{}", Arrays.toString(o));
        log.info("\n{}", Arrays.toString(types));

    }

    @Test
    public void toPrettyString() {
        log.info("bean:\n{}", BeanStringUtil.toPrettyString(obj));
        log.info("extraMeta:\n{}", BeanStringUtil.toPrettyString(obj.getExtraMeta()));

    }

}
