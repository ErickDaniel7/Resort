package com.xpto.resort.exceptions;

public class ForeignKeyConstraintViolationException extends RuntimeException{
    public ForeignKeyConstraintViolationException(String message){
        super (message);
    }
}
