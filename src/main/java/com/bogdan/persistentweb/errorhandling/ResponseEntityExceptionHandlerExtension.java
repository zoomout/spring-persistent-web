package com.bogdan.persistentweb.errorhandling;

import com.bogdan.persistentweb.dto.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ResponseEntityExceptionHandlerExtension extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    final ErrorDetails errorDetails = new ErrorDetails(null, "cannot parse request body");
    return handleExceptionInternal(ex, errorDetails, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    final String errors = ex
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> "validation of field '" + fieldError.getField() + "' failed: " + fieldError.getDefaultMessage())
        .collect(Collectors.joining(","));
    final ErrorDetails errorDetails = new ErrorDetails(null, errors);
    return handleExceptionInternal(ex, errorDetails, headers, HttpStatus.BAD_REQUEST, request);
  }

}