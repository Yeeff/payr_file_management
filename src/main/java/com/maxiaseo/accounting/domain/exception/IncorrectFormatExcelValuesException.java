package com.maxiaseo.accounting.domain.exception;

import java.util.Map;

public class IncorrectFormatExcelValuesException extends RuntimeException{

    private final Map<String, String> items;

    public IncorrectFormatExcelValuesException(String message, Map<String,String> items) {
        super(message);
        this.items = items;
    }

    public Map<String, String> getItems(){
        return items;
    }
}
