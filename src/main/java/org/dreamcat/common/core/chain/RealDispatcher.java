package org.dreamcat.common.core.chain;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ThreadUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiPredicate;

@Slf4j
@NoArgsConstructor
public class RealDispatcher<Req, Res> implements Interceptor.Dispatcher<Req, Res> {

    private final Deque<Interceptor.AsyncCall<Req, Res>> readyAsyncCalls = new ArrayDeque<>();
    private final Deque<Interceptor.AsyncCall<Req, Res>> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<Interceptor.Call<Req, Res>> runningSyncCalls = new ArrayDeque<>();
    private int maxRequests = 64;
    private int maxRequestsForSameCase = 4;
    private BiPredicate<Interceptor.AsyncCall<Req, Res>,
            Interceptor.AsyncCall<Req, Res>> sameCase = (everyCall, currentCall) -> false;
    private Runnable idleCallback = () -> {
        log.debug("dispatcher finished...");
    };
    private ExecutorService executorService;

    public synchronized void enqueue(Interceptor.AsyncCall<Req, Res> call) {
        if (runningAsyncCalls.size() < maxRequests && runningCallsForCase(call) < maxRequestsForSameCase) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    public synchronized void executed(Interceptor.Call<Req, Res> call) {
        runningSyncCalls.add(call);
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = ThreadUtil.newExecutorService("Interceptor.Dispatcher");
        }
        return executorService;
    }

    public void finished(Interceptor.AsyncCall<Req, Res> call) {
        finished(runningAsyncCalls, call, true);
    }

    public void finished(Interceptor.Call<Req, Res> call) {
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

    private int runningCallsForCase(Interceptor.AsyncCall<Req, Res> call) {
        int result = 0;
        for (Interceptor.AsyncCall<Req, Res> c : runningAsyncCalls) {
            if (sameCase.test(c, call)) result++;
        }
        return result;
    }

    private void promoteCalls() {
        if (runningAsyncCalls.size() >= maxRequests) return; // Already running max capacity.
        if (readyAsyncCalls.isEmpty()) return; // No ready calls to promote.

        for (Iterator<Interceptor.AsyncCall<Req, Res>> i
             = readyAsyncCalls.iterator(); i.hasNext(); ) {
            Interceptor.AsyncCall<Req, Res> call = i.next();

            if (runningCallsForCase(call) < maxRequestsForSameCase) {
                i.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }

            if (runningAsyncCalls.size() >= maxRequests) return; // Reached max capacity.
        }
    }

    public synchronized List<Interceptor.Call<Req, Res>> queuedCalls() {
        List<Interceptor.Call<Req, Res>> result = new ArrayList<>();
        for (Interceptor.AsyncCall<Req, Res> asyncCall : readyAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Interceptor.Call<Req, Res>> runningCalls() {
        List<Interceptor.Call<Req, Res>> result = new ArrayList<>(runningSyncCalls);
        for (Interceptor.AsyncCall<Req, Res> asyncCall : runningAsyncCalls) {
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
            Interceptor.AsyncCall<Req, Res>, Interceptor.AsyncCall<Req, Res>> sameCase) {
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

    public synchronized Deque<Interceptor.AsyncCall<Req, Res>> getReadyAsyncCalls() {
        return readyAsyncCalls;
    }

    public synchronized Deque<Interceptor.AsyncCall<Req, Res>> getRunningAsyncCalls() {
        return runningAsyncCalls;
    }

    public synchronized Deque<Interceptor.Call<Req, Res>> getRunningSyncCalls() {
        return runningSyncCalls;
    }
}
