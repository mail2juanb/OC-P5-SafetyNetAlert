package com.oc_P5.SafetyNetAlerts.exceptions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class NotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        NotFoundException exception = new NotFoundException();
        assertEquals("Not Found", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom not found message";
        NotFoundException exception = new NotFoundException(message);
        assertEquals(message, exception.getMessage());
    }
}
