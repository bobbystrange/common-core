package org.dreamcat.common.core;

import org.junit.Test;

/**
 * Create by tuke on 2020/7/22
 */
public class UnionTest {
    @Test
    public void test() {
        Union union = new Union(1);
        System.out.println(union.get(Integer.class));
        union.set(1.0);
        System.out.println(union.get(Double.class));
        union.set("abc");
        System.out.println(union.get(String.class));

    }
}
