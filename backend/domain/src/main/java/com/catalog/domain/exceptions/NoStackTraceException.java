package com.catalog.domain.exceptions;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(String message) {
        super(message, null);
    }

    public NoStackTraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, true, false);
    }


}
