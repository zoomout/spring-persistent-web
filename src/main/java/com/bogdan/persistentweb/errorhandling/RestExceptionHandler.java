package com.bogdan.persistentweb.errorhandling;

import com.bogdan.persistentweb.dto.ErrorDetails;
import com.bogdan.persistentweb.exception.EntityNotFoundException;
import com.bogdan.persistentweb.exception.InvalidPropertyException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity handleConstraintViolationException(
      final HttpServletRequest req,
      final ConstraintViolationException e) {

    final String message = e.getConstraintViolations().stream()
        .map(cv -> {
          final String pathParameterName;
          if (cv.getPropertyPath() instanceof PathImpl) {
            pathParameterName = ((PathImpl) cv.getPropertyPath()).getLeafNode().getName();
          } else {
            pathParameterName = cv.getPropertyPath().toString();
          }
          return "constraint is violated for path parameter '" + pathParameterName + "': " + cv.getMessage();
        })
        .collect(Collectors.joining(","));
    return new ResponseEntity<>(
        new ErrorDetails(null, message),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseBody
  public ResponseEntity handleProductNotFoundException(
      final HttpServletRequest req,
      final EntityNotFoundException e) {
    return new ResponseEntity<>(
        new ErrorDetails(null, e.getMessage()),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(InvalidPropertyException.class)
  @ResponseBody
  public ResponseEntity handleInvalidPropertyException(
      final HttpServletRequest req,
      final InvalidPropertyException e) {
    return new ResponseEntity<>(
        new ErrorDetails(null, e.getMessage()),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseBody
  public ResponseEntity handleNoHandlerFoundException(
      final HttpServletRequest req,
      final NoHandlerFoundException e) {
    return new ResponseEntity<>(
        new ErrorDetails(null, e.getMessage()),
        HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(Throwable.class)
  @ResponseBody
  public ResponseEntity handleException(
      final HttpServletRequest req,
      final Throwable e
  ) {
    e.printStackTrace(); //TODO add logging
    return new ResponseEntity<>(
        new ErrorDetails(null, "An error occurred. Please check the logs for more details."),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
