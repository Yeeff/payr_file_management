package com.maxiaseo.accounting.configuration.exceptionhandler;

import com.maxiaseo.accounting.domain.exception.*;
import com.maxiaseo.accounting.domain.util.ConstantsDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(IncorrectFormatExcelValuesException.class)
    public ResponseEntity<ExceptionResponseList> incorrectFormatExcelValuesExceptionHandler(IncorrectFormatExcelValuesException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionResponseList(
                ex.getMessage(), ex.getItems() , HttpStatus.UNPROCESSABLE_ENTITY.toString(), LocalDateTime.now()));
    }


    @ExceptionHandler(FileRetrievalException.class)
    public ResponseEntity<ExceptionResponse> handleFileRetrievalException(FileRetrievalException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404 if file not found
                .body(new ExceptionResponse( String.format(ConstantsDomain.RETRIEVING_FILE_MESSAGE_ERROR, ex.getMessage()) ,
                        HttpStatus.NOT_FOUND.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleFileProcessingException(FileProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 for processing errors
                .body(new ExceptionResponse(String.format(ConstantsDomain.PROCESSING_FILE_MESSAGE_ERROR, ex.getMessage()) ,
                        HttpStatus.BAD_REQUEST.toString(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(FileSavingException.class)
    public ResponseEntity<ExceptionResponse> handleFileSavingException(FileSavingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(String.format(ConstantsDomain.SAVING_FILE_MESSAGE_ERROR, ex.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(MissingRequiredFileModelFieldException.class)
    public ResponseEntity<ExceptionResponse> handleMissingRequiredFileModelFieldException(MissingRequiredFileModelFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(String.format(ConstantsDomain.NULL_FILE_FIELDS_MESSAGE_ERROR, ex.getMessage()),
                        HttpStatus.BAD_REQUEST.toString(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
