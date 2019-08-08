package com.ragnaroft.utilities.spark_config.util.exception;

public class LoadingFileException extends PropertyException {

    public LoadingFileException(String file, Throwable cause) {
        super("Error loading file: " + file, cause);
    }
}
