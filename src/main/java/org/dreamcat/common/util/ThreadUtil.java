package org.dreamcat.common.util;

import org.dreamcat.common.collection.BlockingTimeoutWorkQueue;
import org.dreamcat.common.collection.BlockingWorkQueue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class ThreadUtil {

    public static <E extends Exception> void submitSync(
            ExecutorService executorService,
            Callable<E> task,
            long timeout, TimeUnit unit,
            Consumer<TimeoutException> callback) throws E {
        Future<E> future = executorService.submit(task);
        try {
            E e = future.get(timeout, unit);
            if (e != null) throw e;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (e instanceof TimeoutException) {
                callback.accept((TimeoutException) e);
            } else {
                throw new RuntimeException(e);
            }
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

    public static ExecutorService newBlockingThreadPool(
            int nThreads, int capacity) {
        return newBlockingThreadPool(nThreads, capacity, false);
    }

    public static ExecutorService newBlockingThreadPool(
            int corePoolSize, int maximumPoolSize, int capacity) {
        return newBlockingThreadPool(corePoolSize, maximumPoolSize, capacity, false);
    }

    public static ExecutorService newBlockingThreadPool(
            int nThreads, int capacity, boolean fair) {
        return new ThreadPoolExecutor(
                nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new BlockingWorkQueue<>(capacity, fair));
    }

    public static ExecutorService newBlockingThreadPool(
            int corePoolSize, int maximumPoolSize, int capacity, boolean fair) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new BlockingWorkQueue<>(capacity, fair));
    }

    public static ExecutorService newBlockingThreadPool(
            int nThreads, int capacity, long timeout, TimeUnit unit, RejectedExecutionHandler handler) {
        return newBlockingThreadPool(nThreads, capacity, false, timeout, unit, handler);
    }

    public static ExecutorService newBlockingThreadPool(
            int corePoolSize, int maximumPoolSize, int capacity,
            long timeout, TimeUnit unit, RejectedExecutionHandler handler) {
        return newBlockingThreadPool(corePoolSize, maximumPoolSize, capacity, false, timeout, unit, handler);
    }

    public static ExecutorService newBlockingThreadPool(
            int nThreads, int capacity, boolean fair,
            long timeout, TimeUnit unit, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new BlockingTimeoutWorkQueue<>(capacity, fair, timeout, unit), handler);
    }

    public static ExecutorService newBlockingThreadPool(
            int corePoolSize, int maximumPoolSize, int capacity, boolean fair,
            long timeout, TimeUnit unit, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new BlockingTimeoutWorkQueue<>(capacity, fair, timeout, unit), handler);
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
