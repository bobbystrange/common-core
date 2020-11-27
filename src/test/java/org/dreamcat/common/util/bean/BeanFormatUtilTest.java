package org.dreamcat.common.util.bean;

import static org.dreamcat.common.core.legacy.BeanFormatUtil.inline;
import static org.dreamcat.common.core.legacy.BeanFormatUtil.pretty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.test.BeanData;
import org.junit.Test;

/**
 * Create by tuke on 2020/3/3
 */
@Slf4j
public class BeanFormatUtilTest {

    private final BeanData.All obj = BeanData.ofAll();
    Object[] oa = new Object[]{1, 3.14, "hello",
            new Date(), LocalDateTime.now(),
            LocalDate.now(),
            LocalTime.now(),
    };

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
    public void testPretty() {
        log.info("bean:\n{}", pretty(obj));
        log.info("inner static:\n{}", pretty(obj.getInnerStatic()));
        log.info("inner:\n{}", pretty(obj.getInner()));
        log.info("int[]:\n{}", pretty(new int[]{1, 3, 6, 10}));
        log.info("double[]:\n{}", pretty(new double[]{1, 3, 6, 10}));
        log.info("long[]:\n{}", pretty(new long[]{1, 3, 6, 10}));

    }

    @Test
    public void testPrettyObjArr() {
        log.info("pretty for T[]:\n{}", pretty(oa));
    }

    @Test
    public void testInline() {
        log.info("bean:\n{}", inline(obj));
        log.info("inner static:\n{}", inline(obj.getInnerStatic()));
        log.info("inner:\n{}", inline(obj.getInner()));
        log.info("int[]:\n{}", inline(new int[]{1, 3, 6, 10}));
        log.info("double[]:\n{}", inline(new double[]{1, 3, 6, 10}));
        log.info("long[]:\n{}", inline(new long[]{1, 3, 6, 10}));
    }

    @Test
    public void testInlineObjArr() {
        log.info("inline for T[]:\n{}", inline(oa));
        log.info("deepToString for T[]:\n{}", Arrays.deepToString(oa));


    }
}
