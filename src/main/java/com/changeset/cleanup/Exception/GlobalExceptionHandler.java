package com.changeset.cleanup.Exception;

import com.changeset.cleanup.Controllers.changesetController;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static  final Logger logger = (Logger) LoggerFactory.getLogger(changesetController.class);

    @ExceptionHandler(IDNotFoundException.class)
    public ResponseEntity<ErrorMessage> IDNotFoundExceptionHandler(Exception e, WebRequest webRequest){
        logger.error(e.getMessage());
        ErrorMessage em = new ErrorMessage(LocalDateTime.now(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(em, HttpStatus.NOT_FOUND);
    }
}
