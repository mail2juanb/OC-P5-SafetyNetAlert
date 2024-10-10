package com.oc_P5.SafetyNetAlerts.exceptions;

public class ArgumentNotValidException extends RuntimeException {

    public ArgumentNotValidException(){
        super("Method Argument Not Valid");
    }

    public ArgumentNotValidException(String message){
        super(message);
    }

}
