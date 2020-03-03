package org.dreamcat.common.bean;

import org.dreamcat.common.core.Pair;
import org.dreamcat.common.core.Triple;
import org.dreamcat.test.BeanData;
import org.junit.Test;

import static org.dreamcat.common.bean.BeanCopyUtil.copy;
import static org.dreamcat.common.bean.BeanFormatUtil.pretty;
import static org.dreamcat.common.util.PrintUtil.printf;
import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/3/3
 */
public class BeanCopyUtilTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        Pair<String, String> p1 = new Pair<>("alice", "hat");
        // copy will not work
        Pair<String, String> p2 = copy(p1, Pair.class);
        assert p2 == null;

        Pair<String, String> p3 = new Pair<>("rabbit", "white queen");
        Triple<String, String, String> t = new Triple<>("cat", p3);

        BeanCopyUtil.copy(p1, p3);
        printf("p1:\t%s\t\n", p1);
        printf("p2:\t%s\t\n", p2);
        printf("p3:\t%s\t\n\n", p3);

        BeanCopyUtil.copy(t, p3);
        printf("t:\t%s\t\n", t);
        printf("p3:\t%s\t\n", p3);
    }

    @Test
    public void testNotSupportDeepClone() {
        BeanData.Pojo source = BeanData.ofPojo();
        BeanData.Pojo target = BeanCopyUtil.copy(source, BeanData.Pojo.class);
        println(pretty(source));
        println(pretty(target));
        target.getD()[0] = -1;
        println(pretty(source));
        println(pretty(target));
    }

}
