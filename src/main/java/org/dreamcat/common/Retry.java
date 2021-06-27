package org.dreamcat.common;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * Create by tuke on 2020/4/13
 */
public class Retry {

    private static final int DEFAULT_TIMES = 3;
    private int times;
    @Getter
    private int retried;

    private Retry() {
        this.times = DEFAULT_TIMES;
    }

    public static Retry ofBlocking(int times) {
        ObjectUtil.requirePositive(times, "times");

        Retry retry = new Retry();
        retry.times = times;
        return retry;
    }

    public boolean retry(Callable<Boolean> callable) {
        return retry(callable, ignore -> {
        });
    }

    public boolean retry(Callable<Boolean> callable, Consumer<Exception> exceptionHandler) {
        while (retried < times) {
            retried++;
            try {
                Boolean result = callable.call();
                if (Objects.equals(result, true)) return true;
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        }
        return false;
    }
}
