package org.dreamcat.common.core.filter;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.println;

/**
 * Create by tuke on 2020/3/29
 */
public class ModalFilterTest {

    @Test
    public void test() {
        RealFilterChain<String, String> filterChain = new RealFilterChain<>(((s1, s2) -> {
            println("service:\t%s, %s\n", s1, s2);
        }));
        filterChain.addFilter(((source, target, chain) -> {
            println("1st:\t%s, %s\n", source, target);
            chain.filter(source.toLowerCase(), "Target");
        })).addFilter(((source, target, chain) -> {
            println("2st:\t%s, %s\n", source, target + target);
            chain.filter(source, target.toUpperCase());
        })).addFilter(((source, target, chain) -> {
            println("3st:\t%s, %s\n", source, target);
            chain.filter(source.toUpperCase(), target);
        })).addFilter(((source, target, chain) -> {
            println("4st:\t%s, %s\n", source, target);
            chain.filter(source, target);
        })).addFilter(((source, target, chain) -> {
            println("5st:\t%s, %s\n", source, target);
            chain.filter(source, target);
        }));
        filterChain.filter("Source", null);
    }

}
