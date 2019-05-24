package org.dreamcat.common.core.chain;

import java.util.List;

public class RealChain<Req, Resp>
        implements Interceptor.Chain<Req, Resp> {

    private final Req original;

    private final List<Interceptor<Req, Resp>> interceptors;

    private final Interceptor.Call<Req, Resp> call;

    private final int index;

    private int calls;

    public RealChain(
            List<Interceptor<Req, Resp>> interceptors,
            int index,
            Req original,
            Interceptor.Call<Req, Resp> call) {
        this.interceptors = interceptors;
        this.index = index;
        this.original = original;
        this.call = call;
    }


    @Override
    public Req original() {
        return original;
    }

    @Override
    public Interceptor.Call<Req, Resp> call() {
        return call;
    }

    public Resp proceed(Req req) throws Exception {
        if (index >= interceptors.size()) throw new AssertionError();

        calls++;

        if (calls > 1) {
            throw new IllegalStateException("interceptor " + interceptors.get(index - 1)
                    + " must call proceed() exactly once");
        }

        // Call the next interceptor in the chain.
        RealChain<Req, Resp> next = new RealChain<>(interceptors, index + 1, req, call);
        Interceptor<Req, Resp> interceptor = interceptors.get(index);
        Resp resp = interceptor.intercept(next);

        // Confirm that the next interceptor made its required call to chain.proceed().
        if (index + 1 < interceptors.size() && next.calls != 1) {
            throw new IllegalStateException("interceptor " + interceptor
                    + " must call proceed() exactly once");
        }

        // Confirm that the intercepted response isn't null.
        if (resp == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }

        return resp;
    }

}
