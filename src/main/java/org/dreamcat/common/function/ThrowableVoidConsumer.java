package org.dreamcat.common.function;

/**
 * Create by tuke on 2020/4/4
 */
@FunctionalInterface
public interface ThrowableVoidConsumer {

    void accept() throws Exception;
}
