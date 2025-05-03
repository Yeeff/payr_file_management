package com.horizonx.file_services.domain.exception;

public class MissingRequiredFileModelFieldException extends RuntimeException {
    public MissingRequiredFileModelFieldException(String message) {
        super(message);
    }
}
