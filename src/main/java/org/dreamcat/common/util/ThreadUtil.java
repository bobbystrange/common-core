package org.dreamcat.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import org.dreamcat.common.function.ThrowableSupplier;
import org.dreamcat.common.function.ThrowableVoidConsumer;

public final class ThreadUtil {

    private ThreadUtil() {
    }

    public static ExecutorService newCallerRunsThreadPool(int nThreads) {
        return newCallerRunsThreadPool(nThreads, nThreads);
    }

    /**
     * create a executor
     *
     * @param corePoolSize    the number of threads to keep alive
     * @param maximumPoolSize the maximum size
     * @return executor
     * @see java.util.concurrent.Executors#newFixedThreadPool(int)
     */
    public static ExecutorService newCallerRunsThreadPool(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                0L, TimeUnit.SECONDS,
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

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public static <V> FutureTask<V> futureTask(
            ThrowableSupplier<V> callable) {
        return new FutureTask<>(() -> {
            try {
                return callable.get();
            } catch (Exception e) {
                return null;
            }
        });
    }

    public static FutureTask<Exception> futureTask(
            ThrowableVoidConsumer runnable) {
        return new FutureTask<>(() -> {
            try {
                runnable.accept();
                return null;
            } catch (Exception e) {
                return e;
            }
        });
    }

    public static <V> V getFutureTask(
            FutureTask<V> submittedTask,
            long timeout,
            TimeUnit unit,
            Consumer<TimeoutException> timeoutCallback) {
        try {
            return submittedTask.get(timeout, unit);
        } catch (TimeoutException e) {
            timeoutCallback.accept(e);
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state...
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getExceptionFutureTask(
            FutureTask<Exception> submittedTask,
            long timeout,
            TimeUnit unit,
            Consumer<TimeoutException> timeoutCallback) {
        Exception exception;
        try {
            exception = submittedTask.get(timeout, unit);
        } catch (TimeoutException e) {
            timeoutCallback.accept(e);
            exception = e;
        } catch (InterruptedException e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            exception = e;
        } catch (Exception e) {
            exception = e;
        }

        if (exception != null) throw new RuntimeException(exception);
    }
}
