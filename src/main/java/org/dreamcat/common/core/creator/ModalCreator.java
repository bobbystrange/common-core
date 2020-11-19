package org.dreamcat.common.core.creator;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.dreamcat.common.function.ThrowableFunction;

/**
 * Create by tuke on 2018-09-09
 */
public interface ModalCreator {

    /**
     * if in cache strategy, then never reuse this method in many services,
     * otherwise a <strong>memory leak</strong> maybe occur
     *
     * @param service the interface which need be proxied
     * @param <S>     interface
     * @return a implementation of the interface
     */
    @SuppressWarnings("unchecked")
    default <S> S create(final Class<S> service) {
        validateService(service);

        if (isEagerly()) {
            for (Method method : service.getDeclaredMethods()) {
                if (!method.isDefault()) {
                    loadServiceMethod(method);
                }
            }
        }

        return (S) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        // interface default method
                        if (method.isDefault()) {
                            Constructor<MethodHandles.Lookup> constructor =
                                    MethodHandles.Lookup.class.getDeclaredConstructor(
                                            Class.class, int.class);

                            constructor.setAccessible(true);
                            return constructor.newInstance(service, -1 /* TRUSTED */)
                                    .unreflectSpecial(method, service)
                                    .bindTo(proxy)
                                    .invokeWithArguments(args);
                        }

                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        return serviceMethod.adapt(args);
                    }
                });
    }

    /**
     * only the interface which only extands Object can be provided
     *
     * @param service the interface which need be proxied
     * @param <S>     interface
     */
    default <S> void validateService(final Class<S> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException(service.getName() + " must be interfaces.");
        }

        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException(
                    service.getName() + " must not extend other interfaces.");
        }
    }

    /**
     * it assumes there will be a cache strategy
     *
     * @return eagerly load ServiceMethod for each method;
     * @see ModalCreator#loadServiceMethod
     */
    default boolean isEagerly() {
        return true;
    }

    /**
     * load ServiceMethod from Method,
     * when the implementation can generate the proxied method
     * by Annotation, Parameter, and ReturnType
     *
     * @param method the method which need be proxied
     * @return proxied method
     */
    ServiceMethod loadServiceMethod(final Method method);

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    interface Call<T> {

        T execute() throws Exception;

        void enqueue(Callback<T> callback);

        boolean isExecuted();

        void cancel();

        boolean isCanceled();

        Object original();

        <R> Call<R> to(ThrowableFunction<T, R> converter);
    }

    interface Callback<T> {

        void onComplete(Call<T> call, T result);

        void onError(Call<T> call, Throwable t);
    }
}
