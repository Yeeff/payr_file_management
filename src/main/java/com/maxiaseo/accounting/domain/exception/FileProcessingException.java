package com.maxiaseo.accounting.domain.exception;

public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}