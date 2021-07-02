package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.LongSupplier }
 *
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpLongSupplier<E extends Exception> {

    long getAsLong() throws E;
}
