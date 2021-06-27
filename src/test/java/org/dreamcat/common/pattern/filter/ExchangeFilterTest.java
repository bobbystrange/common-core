package org.dreamcat.common.pattern.filter;

import org.dreamcat.common.util.StringUtil;
import org.junit.Test;

/**
 * Create by tuke on 2020/3/29
 */
public class ExchangeFilterTest {

    @Test
    public void test() {
        ExchangeFilterChain<String> filterChain = new ExchangeFilterChain<>(((s) -> {
            System.out.printf("service:\t%s\n", s);
        }));
        filterChain.addFilter(((source, chain) -> {
            System.out.printf("1st:\t%s\n", source);
            chain.filter(source.toLowerCase());
        })).addFilter(((source, chain) -> {
            System.out.printf("2st:\t%s\n", source);
            chain.filter(source.toUpperCase());
        })).addFilter(((source, chain) -> {
            System.out.printf("3st:\t%s\n", source);
            chain.filter(source.toUpperCase() + source.toLowerCase());
        })).addFilter(((source, chain) -> {
            System.out.printf("4st:\t%s\n", source);
            chain.filter(StringUtil.reverse(source));
        })).addFilter(((source, chain) -> {
            System.out.printf("5st:\t%s\n", source);
            chain.filter(source);
        }));
        filterChain.filter("Source");
    }

}
