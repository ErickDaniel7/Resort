package com.xpto.resort.exceptions;

public class UniqueConstraintViolationException extends RuntimeException{
    public UniqueConstraintViolationException(String message){
        super(message);
    }
}
