package org.dreamcat.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class ThreadUtil {

    public static void submitAndGet(
            FutureTask<Exception> task,
            long timeout,
            TimeUnit unit,
            Consumer<TimeoutException> timeoutCallback) {
        try {
            Executors.newSingleThreadExecutor().submit(task);
            Exception exception = task.get(timeout, unit);
            if (exception != null) throw new RuntimeException(exception);
        } catch (Exception e) {
            if (e instanceof TimeoutException) {
                timeoutCallback.accept((TimeoutException) e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static ExecutorService newExecutorService(String name) {
        return new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                newThreadFactory(name, false));
    }

    public static ThreadFactory newThreadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

}
