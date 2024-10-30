package com.maxiaseo.accounting.configuration.exceptionhandler;

import com.maxiaseo.accounting.domain.exception.IncorrectFormatExcelValuesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(IncorrectFormatExcelValuesException.class)
    public ResponseEntity<ExceptionResponse> IncorrectFormatExcelValuesExceptionHandler(IncorrectFormatExcelValuesException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }
}
