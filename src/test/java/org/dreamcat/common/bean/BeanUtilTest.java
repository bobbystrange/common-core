package org.dreamcat.common.bean;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.core.Pair;
import org.dreamcat.common.core.Triple;
import org.dreamcat.common.test.BeanBase;
import org.junit.Test;

import java.util.Arrays;

import static org.dreamcat.common.bean.BeanUtil.*;

@Slf4j
public class BeanUtilTest {

    private BeanBase obj = BeanBase.newInstance();

    @SuppressWarnings("unchecked")
    @Test
    public void copyTest() {

        Pair<String, String> p1 = new Pair<>("alice", "hat");
        // copy will not work
        Pair<String, String> p2 = copy(p1, Pair.class);
        assert p2 == null;

        Pair<String, String> p3 = new Pair<>("rabbit", "white queen");
        Triple<String, String, String> t = new Triple<>("cat", p3);

        copyProperties(p1, p3);
        System.out.printf("p1:\t%s\t\n", p1);
        System.out.printf("p2:\t%s\t\n", p2);
        System.out.printf("p3:\t%s\t\n\n", p3);

        copyProperties(t, p3);
        System.out.printf("t:\t%s\t\n", t);
        System.out.printf("p3:\t%s\t\n", p3);
    }

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
    public void prettyString() {
        log.info("bean:\n{}", toPrettyString(obj));
        log.info("extraMeta:\n{}", toPrettyString(obj.getExtraMeta()));

    }

}
