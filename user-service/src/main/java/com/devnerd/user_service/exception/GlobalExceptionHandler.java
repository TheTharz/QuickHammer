package com.devnerd.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResources(DuplicateResourceException ex){
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        409,
        System.currentTimeMillis()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }
}
