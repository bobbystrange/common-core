package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.DoubleSupplier }
 *
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpDoubleSupplier<E extends Exception> {

    double getAsDouble() throws E;
}
