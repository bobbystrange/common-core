package org.dreamcat.common.pattern.creator;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Create by tuke on 2018-09-09
 */
@RequiredArgsConstructor
@Setter
public class RealCreator implements ModalCreator {

    private final Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();
    private final BiFunction<Method, Object[], Object> methodAdapter;
    private boolean eagerly;

    public ServiceMethod loadServiceMethod(final Method method) {
        ServiceMethod result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new CreatorServiceMethod(this, method, methodAdapter);
                serviceMethodCache.put(method, result);
            }
            return result;
        }
    }

    @Override
    public boolean isEagerly() {
        return eagerly;
    }

}
