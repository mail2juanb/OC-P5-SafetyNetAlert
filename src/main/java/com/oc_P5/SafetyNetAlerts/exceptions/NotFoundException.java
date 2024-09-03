package com.oc_P5.SafetyNetAlerts.exceptions;

public class NotFoundException  extends RuntimeException {
    public NotFoundException(){
        super("Not Found");
    }

    public NotFoundException(String message){
        super(message);
    }
}
