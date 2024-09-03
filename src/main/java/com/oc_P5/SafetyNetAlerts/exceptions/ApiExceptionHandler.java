package com.oc_P5.SafetyNetAlerts.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleConflictException(ConflictException exception){
        return createErrorResponse(exception, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException exception){
        return  createErrorResponse(exception, HttpStatus.NOT_FOUND);

    }

    private static ErrorResponse createErrorResponse(RuntimeException exception, HttpStatus status) {
        return ErrorResponse.builder(exception, status, exception.getMessage())
                .property("timestamp", Instant.now())
                .build();
    }
}
