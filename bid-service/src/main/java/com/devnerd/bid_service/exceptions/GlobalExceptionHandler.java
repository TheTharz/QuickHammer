package com.devnerd.bid_service.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 500, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDatabaseIntegrityException(DataIntegrityViolationException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 409, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
}
