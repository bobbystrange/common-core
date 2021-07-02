package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.Supplier }
 *
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpSupplier<T, E extends Exception> {

    T get() throws E;
}
