package org.dreamcat.common.exception;

import lombok.RequiredArgsConstructor;

/**
 * Create by tuke on 2020/5/30
 */
@RequiredArgsConstructor
public class BreakException extends Exception {
    private final Object data;

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
}
