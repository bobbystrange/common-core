package com.tukeof.common.core.creator;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * Create by tuke on 2018-09-09
 */
public class CreatorServiceMethod implements Creator.ServiceMethod {

    private final Creator creator;
    private final Method method;
    private final BiFunction<Method, Object[], Object> methodAdapter;

    public CreatorServiceMethod(Creator creator, Method method, BiFunction<Method, Object[], Object> methodAdapter) {
        this.creator = creator;
        this.method = method;
        this.methodAdapter = methodAdapter;
    }

    public Creator creator() {
        return creator;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Creator.Call<?> adapt(Object[] args) throws Exception {
        Object result = methodAdapter.apply(method, args);

        return CreatorCall.newCall(result, it -> it);
    }
}
