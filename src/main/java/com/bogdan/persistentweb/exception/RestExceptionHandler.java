package com.bogdan.persistentweb.exception;

import com.bogdan.persistentweb.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity handleConstraintViolationException(
            final HttpServletRequest req,
            final ConstraintViolationException e) {
        List<ErrorDetails> errors = e.getConstraintViolations().stream()
                .map(cv -> new ErrorDetails(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity handleException(
            final HttpServletRequest req,
            final Throwable e
    ) {
        return new ResponseEntity<>("An error occurred. Check logs.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}