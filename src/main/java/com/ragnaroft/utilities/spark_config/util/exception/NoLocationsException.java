package com.ragnaroft.utilities.spark_config.util.exception;

public class NoLocationsException extends PropertyException {

    public NoLocationsException() {
        super("The parameter 'locations' can't be null or empty.");
    }
}
