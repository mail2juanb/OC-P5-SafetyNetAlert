package com.oc_P5.SafetyNetAlerts.exceptions;


public class NullOrEmptyObjectException extends RuntimeException {

    public NullOrEmptyObjectException() {
        super("NullOrEmptyObject");
    }

    public NullOrEmptyObjectException(String message) {
        super(message);
    }

}
