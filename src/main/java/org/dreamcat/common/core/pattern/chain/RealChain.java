package org.dreamcat.common.core.pattern.chain;

import java.util.List;

public class RealChain<I, O>
        implements Interceptor.Chain<I, O> {

    private final I original;

    private final List<Interceptor<I, O>> interceptors;

    private final Interceptor.Call<I, O> call;

    private final int index;

    private int calls;

    public RealChain(
            List<Interceptor<I, O>> interceptors,
            int index,
            I original,
            Interceptor.Call<I, O> call) {
        this.interceptors = interceptors;
        this.index = index;
        this.original = original;
        this.call = call;
    }

    @Override
    public I original() {
        return original;
    }

    @Override
    public Interceptor.Call<I, O> call() {
        return call;
    }

    @Override
    public O proceed(I input) throws Exception {
        if (index >= interceptors.size()) throw new AssertionError();

        calls++;

        if (calls > 1) {
            throw new IllegalStateException(INTERCEPTOR + (index - 1)
                    + " must call proceed() exactly once");
        }

        // Call the next interceptor in the chain.
        RealChain<I, O> next = new RealChain<>(interceptors, index + 1, input, call);
        Interceptor<I, O> interceptor = interceptors.get(index);
        O o = interceptor.intercept(next);

        // Confirm that the next interceptor made its required call to chain.proceed().
        if (index + 1 < interceptors.size() && next.calls != 1) {
            throw new IllegalStateException(INTERCEPTOR + index
                    + " must call proceed() exactly once");
        }

        // Confirm that the intercepted response isn't null.
        if (o == null) {
            throw new NullPointerException(INTERCEPTOR + index + " returned null");
        }

        return o;
    }

    private static final String INTERCEPTOR = "interceptor";
}
