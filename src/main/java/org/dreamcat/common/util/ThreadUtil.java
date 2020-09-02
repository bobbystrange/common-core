package org.dreamcat.common.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class ThreadUtil {

    public static <V> void submitSync(
            ExecutorService executorService,
            Callable<V> task,
            long timeout, TimeUnit unit,
            Consumer<V> callback,
            Consumer<TimeoutException> timeoutCallback) {
        Future<V> future = executorService.submit(task);
        try {
            V result = future.get(timeout, unit);
            callback.accept(result);
        } catch (TimeoutException e) {
            timeoutCallback.accept(e);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void submitSync(
            ExecutorService executorService,
            Runnable task,
            long timeout, TimeUnit unit,
            Consumer<TimeoutException> callback) {
        Future<?> future = executorService.submit(task);
        try {
            future.get(timeout, unit);
        } catch (TimeoutException e) {
            callback.accept(e);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static ExecutorService newCallerRunsThreadPool(int nThreads) {
        return new ThreadPoolExecutor(
                nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ExecutorService newCallerRunsThreadPool(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static ThreadFactory newThreadFactory(final String name) {
        return newThreadFactory(name, false);
    }

    public static ThreadFactory newThreadFactory(final String name, final boolean daemon) {
        return runnable -> {
            Thread result = new Thread(runnable, name);
            result.setDaemon(daemon);
            return result;
        };
    }

}
