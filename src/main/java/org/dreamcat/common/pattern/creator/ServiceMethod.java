package org.dreamcat.common.pattern.creator;

import java.lang.reflect.Method;

/**
 * Create by tuke on 2018-09-09
 */
public interface ServiceMethod {

    Method method();

    Object adapt(Object[] args) throws Exception;
}
