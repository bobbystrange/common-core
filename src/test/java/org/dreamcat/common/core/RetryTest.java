package org.dreamcat.common.core;

import org.junit.Test;

import static org.dreamcat.common.util.PrintUtil.log;

/**
 * Create by tuke on 2020/4/13
 */
public class RetryTest {

    @Test
    public void test() {
        Retry retry = Retry.ofBlocking(7);
        boolean applied = retry.retry(() -> {
            return Math.random() > 0.9;
        });
        log("Retried {} times, result is {}", retry.getRetried(), applied);
    }
}
