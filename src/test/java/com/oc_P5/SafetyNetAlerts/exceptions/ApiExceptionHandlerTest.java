package com.oc_P5.SafetyNetAlerts.exceptions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @Mock
    private WebRequest webRequest;


    @Test
    void handleConflictException_shouldReturnConflictResponse() {
        // Given a Conflict exception
        ConflictException conflictException = new ConflictException("Conflict occurred");

        // When call the method
        ResponseEntity<?> response = apiExceptionHandler.handleConflictException(conflictException, webRequest);

        // Then response HttpStatus is 409 - Conflict
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleNotFoundException_shouldReturnNotFoundResponse() {
        // Given a NotFoundException
        NotFoundException notFoundException = new NotFoundException("Not found");

        // When call the method
        ResponseEntity<?> response = apiExceptionHandler.handleNotFoundException(notFoundException, webRequest);

        // Then response HttpStatus is 404 - Conflict
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}