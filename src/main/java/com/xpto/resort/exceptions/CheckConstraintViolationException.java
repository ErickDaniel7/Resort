package com.xpto.resort.exceptions;

public class CheckConstraintViolationException extends RuntimeException{

    public CheckConstraintViolationException(String message){
        super(message);
    }
}
