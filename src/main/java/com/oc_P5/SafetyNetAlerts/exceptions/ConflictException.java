package com.oc_P5.SafetyNetAlerts.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException(){
        super("Conflict");
    }

    public ConflictException(String message){
        super(message);
    }

}
