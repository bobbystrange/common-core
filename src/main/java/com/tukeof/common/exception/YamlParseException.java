package com.tukeof.common.exception;

/**
 * Create by tuke on 2019-01-10
 */
public class YamlParseException extends Exception {

    public YamlParseException(){
        super();
    }

    public YamlParseException(String message){
        super(message);
    }

    public YamlParseException(String message, Throwable cause){
        super(message, cause);
    }

    public YamlParseException(Throwable cause){
        super(cause);
    }
}
