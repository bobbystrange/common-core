package com.tukeof.common.exception;

/**
 * Create by tuke on 2018-12-27
 */
public class InvalidBeanExpressionException extends RuntimeException {

    public InvalidBeanExpressionException() {
        super();
    }

    public InvalidBeanExpressionException(String message) {
        super(message);
    }

    public InvalidBeanExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBeanExpressionException(Throwable cause) {
        super(cause);
    }
}
