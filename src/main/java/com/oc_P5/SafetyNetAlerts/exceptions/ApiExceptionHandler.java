package com.oc_P5.SafetyNetAlerts.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException exception, WebRequest request){
        return createErrorResponse(exception, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request){
        return  createErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }



    private ResponseEntity<Object> createErrorResponse(Exception exception, HttpStatus status, WebRequest request) {
        HttpStatusCode statusCode = HttpStatusCode.valueOf(status.value());
        return handleExceptionInternal(exception, null, new HttpHeaders(), statusCode,  request);
    }



    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        HttpStatus status = HttpStatus.valueOf(statusCode.value());

        Map<String , Object> errorFields = new HashMap<>();

        // Gérer les erreurs de validation des @RequestBody
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException){
            methodArgumentNotValidException.getBindingResult().getAllErrors()
                    .forEach(e -> errorFields.put( ((FieldError) e).getField(), e.getDefaultMessage()) );
        }

        // Gérer les erreurs de validation des @RequestParam
        if (exception instanceof HandlerMethodValidationException handlerMethodValidationException) {

            // Récupérer tous les résultats de validation
            List<ParameterValidationResult> validationResults = handlerMethodValidationException.getAllValidationResults();

            // Extraire les noms de paramètres
            List<String> parameterNames = validationResults.stream()
                    .map(result -> result.getMethodParameter().getParameterName())
                    .distinct() // Pour éviter les doublons
                    .toList();

            // Gérer les erreurs de validation
            handlerMethodValidationException.getAllErrors().forEach(e -> {
                if (e instanceof FieldError fieldError) {
                    errorFields.put(fieldError.getField(), fieldError.getDefaultMessage());
                } else if (e instanceof DefaultMessageSourceResolvable resolvable) {
                    // Si le résolvable a un paramètre, l'utiliser, sinon prendre le premier nom de paramètre
                    String key = parameterNames.isEmpty() ? "unknown" : parameterNames.get(0);
                    errorFields.put(key, resolvable.getDefaultMessage());
                } else {
                    // Logger si nécessaire
                    log.warn("Unexpected error type: " + e.getClass().getSimpleName());
                }
            });
        }

        // Construire la réponse d'erreur
        ErrorResponse response = ErrorResponse.builder(exception, status, exception.getMessage())
                .property("errorFields" , errorFields)
                .property("timestamp", Instant.now())
                .build();


        return  ResponseEntity.status(statusCode).body(response);
    }

}