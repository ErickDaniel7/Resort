package com.xpto.resort.exceptions;

public class NotNullConstraintViolationException extends RuntimeException{

    public NotNullConstraintViolationException(String message){
        super(message);
    }
}
