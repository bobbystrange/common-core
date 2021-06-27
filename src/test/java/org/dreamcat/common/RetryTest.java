package org.dreamcat.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create by tuke on 2020/4/13
 */
@Slf4j
public class RetryTest {

    @Test
    public void test() {
        Retry retry = Retry.ofBlocking(7);
        boolean applied = retry.retry(() -> {
            return Math.random() > 0.9;
        });
        log.info("Retried {} times, result is {}", retry.getRetried(), applied);
    }
}
