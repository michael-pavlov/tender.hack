package ru.gitpush.tender.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(value = "ru.gitpush.tender")
public class BaseController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity otherError(Exception exp) {
        log.error("There is an exception:", exp);
        return new ResponseEntity<>("Error due to exception: " + exp.toString(), HttpStatus.BAD_REQUEST);
    }
}
