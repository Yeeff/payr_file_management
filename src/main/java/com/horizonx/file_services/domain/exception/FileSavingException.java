package com.horizonx.file_services.domain.exception;

public class FileSavingException extends RuntimeException {

    public FileSavingException(String message) {
        super(message);
    }

    public FileSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}