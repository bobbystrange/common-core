package org.dreamcat.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {

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
