package com.ragnaroft.utilities.spark_config.util.exception;

public class PropertyException extends RuntimeException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(Throwable t) {
        super(t);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
