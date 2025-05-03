package com.horizonx.file_services.configuration.exceptionhandler;


import java.time.LocalDateTime;
import java.util.Map;


public class ExceptionResponseList {

    private final String message;
    private final String status;
    private final LocalDateTime timestamp;
    private final Map<String, String> items;

    public ExceptionResponseList(String message, Map<String, String> items, String status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getItems() {
        return items;
    }

}
