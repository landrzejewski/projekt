package com.example.restdockerplatform.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IncorrectFileTypeException.class)
    ResponseEntity<?> handleIOException(IncorrectFileTypeException ex) {

        log.error(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .build();
    }


    @ExceptionHandler(value = IOException.class)
    ResponseEntity<String> handleIOException(IOException ex) {

        log.error(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .build();
    }

}