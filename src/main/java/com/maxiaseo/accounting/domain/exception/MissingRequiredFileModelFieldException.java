package com.maxiaseo.accounting.domain.exception;

public class MissingRequiredFileModelFieldException extends RuntimeException {
    public MissingRequiredFileModelFieldException(String message) {
        super(message);
    }
}
