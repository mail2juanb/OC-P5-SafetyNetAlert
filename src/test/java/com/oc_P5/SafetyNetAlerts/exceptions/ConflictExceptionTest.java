package com.oc_P5.SafetyNetAlerts.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConflictExceptionTest {

    @Test
    void testDefaultConstructor() {
        ConflictException exception = new ConflictException();
        assertEquals("Conflict", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom conflict message";
        ConflictException exception = new ConflictException(message);
        assertEquals(message, exception.getMessage());
    }

}
