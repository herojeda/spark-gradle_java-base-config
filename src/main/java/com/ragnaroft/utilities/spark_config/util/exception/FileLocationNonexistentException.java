package com.ragnaroft.utilities.spark_config.util.exception;

public class FileLocationNonexistentException extends PropertyException {

    public FileLocationNonexistentException(String file) {
        super("Unable to find file: " + file);
    }
}
