package com.maxiaseo.accounting.domain.exception;

public class FileSavingException extends RuntimeException {

    public FileSavingException(String message) {
        super(message);
    }

    public FileSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}