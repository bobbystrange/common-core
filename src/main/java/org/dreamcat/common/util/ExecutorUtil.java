package org.dreamcat.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Create by tuke on 2019-03-31
 */
public class ExecutorUtil {

    public static void submitAndGet(
            FutureTask<Exception> task,
            long timeout,
            TimeUnit unit,
            Consumer<TimeoutException> timeoutCallback){
        try {
            Executors.newSingleThreadExecutor().submit(task);
            Exception exception = task.get(timeout, unit);
            if (exception != null) throw new RuntimeException(exception);
        } catch (Exception e) {
            if (e instanceof TimeoutException){
                timeoutCallback.accept((TimeoutException) e);
            }
            throw new RuntimeException(e);
        }
    }
}
