package org.dreamcat.common.function;

/**
 * see also {@link java.util.function.IntSupplier }
 *
 * @author Jerry Will
 * @since 2021-07-02
 */
@FunctionalInterface
public interface ExpIntSupplier<E extends Exception> {

    int getAsInt() throws E;
}
