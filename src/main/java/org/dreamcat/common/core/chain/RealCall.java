package org.dreamcat.common.core.chain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RealCall<Req, Res>
        implements Interceptor.Call<Req, Res> {

    private final Req original;

    private final InterceptTarget<Req, Res> target;

    private final Interceptor.Dispatcher<Req, Res> dispatcher;

    private final Interceptor.Listener<Req, Res> listener;

    private boolean executed;

    private volatile boolean canceled;

    private RealCall(InterceptTarget<Req, Res> target, Req original) {
        this.target = target;
        this.dispatcher = target.dispatcher();
        this.listener = target.listener();
        this.original = original;
    }

    public static <Resp, Req> RealCall<Req, Resp> newCall(InterceptTarget<Req, Resp> interceptTarget, Req originalRequest) {
        return new RealCall<>(interceptTarget, originalRequest);
    }

    @Override
    public Req original() {
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
    public Res execute() throws Exception {
        synchronized (this) {
            if (executed) throw new IllegalStateException("already executed");
            executed = true;
        }
        listener.onBefore(this);
        try {
            dispatcher.executed(this);
            Res result = getResponseWithChain();
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
    public void enqueue(Interceptor.Callback<Req, Res> respCallback) {
        synchronized (this) {
            if (executed) throw new IllegalStateException("already executed");
            executed = true;
        }
        dispatcher.enqueue(new AsyncCall(respCallback));
    }

    private Res getResponseWithChain() throws Exception {
        List<Interceptor<Req, Res>> interceptors = new ArrayList<>(target.interceptors());
        interceptors.add((RealInterceptTarget<Req, Res>) target);

        Interceptor.Chain<Req, Res> chain = new RealChain<>(
                interceptors, 0, original, this);

        return chain.proceed(original);
    }

    class AsyncCall implements Interceptor.AsyncCall<Req, Res> {

        private final Interceptor.Callback<Req, Res> callback;

        private final String name;

        AsyncCall(Interceptor.Callback<Req, Res> callback) {
            this.callback = callback;
            this.name = target.originalName(original);
        }

        public Req original() {
            return original;
        }

        public Interceptor.Call<Req, Res> get() {
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
                Res res = getResponseWithChain();
                if (isCanceled()) {
                    signalledCallback = true;

                    callback.onError(RealCall.this, new Exception("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onComptele(RealCall.this, res);
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
