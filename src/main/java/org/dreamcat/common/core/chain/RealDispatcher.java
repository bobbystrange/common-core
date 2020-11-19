package org.dreamcat.common.core.chain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiPredicate;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ThreadUtil;

@Slf4j
@NoArgsConstructor
public class RealDispatcher<I, O> implements Interceptor.Dispatcher<I, O> {

    private final Deque<Interceptor.AsyncCall<I, O>> readyAsyncCalls = new ArrayDeque<>();
    private final Deque<Interceptor.AsyncCall<I, O>> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<Interceptor.Call<I, O>> runningSyncCalls = new ArrayDeque<>();
    private int maxRequests = 64;
    private int maxRequestsForSameCase = 4;
    private BiPredicate<Interceptor.AsyncCall<I, O>,
            Interceptor.AsyncCall<I, O>> sameCase = (everyCall, currentCall) -> false;
    private Runnable idleCallback = () -> {
        if (log.isDebugEnabled()) log.debug("Interceptor.Dispatcher finished...");
    };
    private ExecutorService executorService;

    public synchronized void enqueue(Interceptor.AsyncCall<I, O> call) {
        if (runningAsyncCalls.size() < maxRequests &&
                runningCallsForCase(call) < maxRequestsForSameCase) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    public synchronized void executed(Interceptor.Call<I, O> call) {
        runningSyncCalls.add(call);
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = ThreadUtil.newCallerRunsThreadPool(
                    0, Runtime.getRuntime().availableProcessors());
        }
        return executorService;
    }

    public void finished(Interceptor.AsyncCall<I, O> call) {
        finished(runningAsyncCalls, call, true);
    }

    public void finished(Interceptor.Call<I, O> call) {
        finished(runningSyncCalls, call, false);
    }

    private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
        int runningCallsCount;
        Runnable idleCallback;
        synchronized (this) {
            if (!calls.remove(call))
                throw new AssertionError("Call wasn't in-flight!");
            if (promoteCalls)
                promoteCalls();

            runningCallsCount = runningCallsCount();
            idleCallback = this.idleCallback;
        }

        if (runningCallsCount == 0 && idleCallback != null) {
            idleCallback.run();
        }
    }

    private int runningCallsForCase(Interceptor.AsyncCall<I, O> call) {
        int result = 0;
        for (Interceptor.AsyncCall<I, O> c : runningAsyncCalls) {
            if (sameCase.test(c, call)) result++;
        }
        return result;
    }

    private void promoteCalls() {
        if (runningAsyncCalls.size() >= maxRequests) return; // Already running max capacity.
        if (readyAsyncCalls.isEmpty()) return; // No ready calls to promote.

        for (Iterator<Interceptor.AsyncCall<I, O>> i
                = readyAsyncCalls.iterator(); i.hasNext(); ) {
            Interceptor.AsyncCall<I, O> call = i.next();

            if (runningCallsForCase(call) < maxRequestsForSameCase) {
                i.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }

            if (runningAsyncCalls.size() >= maxRequests) return; // Reached max capacity.
        }
    }

    public synchronized List<Interceptor.Call<I, O>> queuedCalls() {
        List<Interceptor.Call<I, O>> result = new ArrayList<>();
        for (Interceptor.AsyncCall<I, O> asyncCall : readyAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Interceptor.Call<I, O>> runningCalls() {
        List<Interceptor.Call<I, O>> result = new ArrayList<>(runningSyncCalls);
        for (Interceptor.AsyncCall<I, O> asyncCall : runningAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized int queuedCallsCount() {
        return readyAsyncCalls.size();
    }

    public synchronized int runningCallsCount() {
        return runningAsyncCalls.size() + runningSyncCalls.size();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public synchronized void setIdleCallback(Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    public synchronized void setSameCase(BiPredicate<
            Interceptor.AsyncCall<I, O>, Interceptor.AsyncCall<I, O>> sameCase) {
        this.sameCase = sameCase;
    }

    public synchronized int getMaxRequests() {
        return maxRequests;
    }

    public synchronized void setMaxRequests(int maxRequests) {
        if (maxRequests < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequests);
        }
        this.maxRequests = maxRequests;
        promoteCalls();
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    public synchronized int getMaxRequestsForSameCase() {
        return maxRequestsForSameCase;
    }

    public synchronized void setMaxRequestsForSameCase(int maxRequestsForSameCase) {
        this.maxRequestsForSameCase = maxRequestsForSameCase;
    }

    public synchronized ExecutorService getExecutorService() {
        return executorService;
    }

    public synchronized void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public synchronized Deque<Interceptor.AsyncCall<I, O>> getReadyAsyncCalls() {
        return readyAsyncCalls;
    }

    public synchronized Deque<Interceptor.AsyncCall<I, O>> getRunningAsyncCalls() {
        return runningAsyncCalls;
    }

    public synchronized Deque<Interceptor.Call<I, O>> getRunningSyncCalls() {
        return runningSyncCalls;
    }
}
