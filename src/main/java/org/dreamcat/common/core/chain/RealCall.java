package org.dreamcat.common.core.chain;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealCall<I, O>
        implements Interceptor.Call<I, O> {

    private final I original;

    private final InterceptTarget<I, O> target;

    private final Interceptor.Dispatcher<I, O> dispatcher;

    private final Interceptor.Listener<I, O> listener;

    private boolean executed;

    private volatile boolean canceled;

    private RealCall(InterceptTarget<I, O> target, I original) {
        this.target = target;
        this.dispatcher = target.dispatcher();
        this.listener = target.listener();
        this.original = original;
    }

    public static <Resp, Req> RealCall<Req, Resp> newCall(
            InterceptTarget<Req, Resp> interceptTarget, Req originalRequest) {
        return new RealCall<>(interceptTarget, originalRequest);
    }

    @Override
    public I original() {
        return original;
    }

    @Override
    public void cancel() {
        canceled = true;
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public synchronized boolean isCanceled() {
        return canceled;
    }

    @Override
    public O execute() throws Exception {
        synchronized (this) {
            if (executed) throw new IllegalStateException("already executed");
            executed = true;
        }
        listener.onBefore(this);
        try {
            dispatcher.executed(this);
            O result = getResponseWithChain();
            if (result == null) throw new Exception("canceled");
            listener.onReturn(this, result);
            return result;
        } catch (Exception e) {
            listener.onThrow(this, e);
            throw e;
        } finally {
            dispatcher.finished(this);
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public void enqueue(Interceptor.Callback<I, O> respCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("already executed");
            executed = true;
        }
        dispatcher.enqueue(new AsyncCall(respCallback));
    }

    private O getResponseWithChain() throws Exception {
        List<Interceptor<I, O>> interceptors = new ArrayList<>(target.interceptors());
        interceptors.add((RealInterceptTarget<I, O>) target);

        Interceptor.Chain<I, O> chain = new RealChain<>(
                interceptors, 0, original, this);

        return chain.proceed(original);
    }

    class AsyncCall implements Interceptor.AsyncCall<I, O> {

        private final Interceptor.Callback<I, O> callback;

        private final String name;

        AsyncCall(Interceptor.Callback<I, O> callback) {
            this.callback = callback;
            this.name = target.originalName(original);
        }

        public I original() {
            return original;
        }

        public Interceptor.Call<I, O> get() {
            return RealCall.this;
        }

        @Override
        public final void run() {
            String oldName = Thread.currentThread().getName();
            Thread.currentThread().setName(name);
            try {
                execute();
            } finally {
                Thread.currentThread().setName(oldName);
            }
        }

        public void execute() {
            boolean signalledCallback = false;
            try {
                O o = getResponseWithChain();
                if (isCanceled()) {
                    signalledCallback = true;

                    callback.onError(RealCall.this, new Exception("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onComptele(RealCall.this, o);
                }
            } catch (Exception e) {
                if (signalledCallback) {
                    // Do not signal the callback twice!
                    log.error("callback failure", e);
                } else {
                    listener.onThrow(RealCall.this, e);
                    callback.onError(RealCall.this, e);
                }
            } finally {
                dispatcher.finished(this);
            }
        }

    }
}
