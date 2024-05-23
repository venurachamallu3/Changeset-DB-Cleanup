package com.changeset.cleanup.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IDNotFoundException extends RuntimeException{

//    private String message;
    public IDNotFoundException(String message){
        super(message);
    }
}
