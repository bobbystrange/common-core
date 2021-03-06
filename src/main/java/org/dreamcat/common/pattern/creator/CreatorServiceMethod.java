package org.dreamcat.common.pattern.creator;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * Create by tuke on 2018-09-09
 */
public class CreatorServiceMethod implements ServiceMethod {

    private final ModalCreator creator;
    private final Method method;
    private final BiFunction<Method, Object[], Object> methodAdapter;

    public CreatorServiceMethod(
            ModalCreator creator, Method method,
            BiFunction<Method, Object[], Object> methodAdapter) {
        this.creator = creator;
        this.method = method;
        this.methodAdapter = methodAdapter;
    }

    public ModalCreator creator() {
        return creator;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public ModalCreator.Call<?> adapt(Object[] args) throws Exception {
        Object result = methodAdapter.apply(method, args);
        return CreatorCall.newCall(result, it -> it);
    }
}
