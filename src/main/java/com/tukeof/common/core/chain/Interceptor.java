package com.tukeof.common.core.chain;

public interface Interceptor<Req, Resp> {

    Resp intercept(Chain<Req, Resp> chain) throws Exception;

    interface Chain<Req, Resp> {

        Req original();

        Resp proceed(Req req) throws Exception;

        Call<Req, Resp> call();
    }

    interface Call<Req, Resp> extends Cloneable {

        Req original();

        Resp execute() throws Exception;

        void enqueue(Interceptor.Callback<Req, Resp> respCallback);

        void cancel();

        boolean isExecuted();

        boolean isCanceled();
    }

    interface AsyncCall<Req, Resp> extends Runnable {

        Req original();

        void execute();

        Call<Req, Resp> get();
    }

    interface Callback<Req, Resp> {

        void onComptele(RealCall<Req, Resp> call, Resp resp);

        void onError(RealCall<Req, Resp> call, Exception e);
    }

    // only works on synchronous call
    interface Listener<Req, Resp> {

        default void onBefore(Call<Req, Resp> call) {
        }

        default void onReturn(Call<Req, Resp> call, Resp result) {
        }

        default void onThrow(Call<Req, Resp> call, Exception e) {
        }
    }

    interface Dispatcher<Req, Resp> {

        void enqueue(AsyncCall<Req, Resp> call);

        void executed(Call<Req, Resp> call);

        void finished(AsyncCall<Req, Resp> call);

        void finished(Call<Req, Resp> call);

    }
}
