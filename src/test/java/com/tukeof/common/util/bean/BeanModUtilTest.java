package com.tukeof.common.util.bean;

import com.tukeof.common.core.Pair;
import com.tukeof.common.core.Triple;
import org.junit.Test;

import static com.tukeof.common.util.bean.BeanModUtil.copy;
import static com.tukeof.common.util.bean.BeanModUtil.copyProperties;

public class BeanModUtilTest {

    @SuppressWarnings("unchecked")
    @Test
    public void copyTest() throws IllegalAccessException {

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


}
