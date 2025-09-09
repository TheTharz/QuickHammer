package com.devnerd.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(WeakPasswordException.class)
  public ResponseEntity<ErrorResponse> handleWeakPassword(WeakPasswordException ex){
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
}
