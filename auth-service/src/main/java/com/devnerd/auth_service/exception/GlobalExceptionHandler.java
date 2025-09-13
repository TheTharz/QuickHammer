package com.devnerd.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(WeakPasswordException.class)
  public ResponseEntity<ErrorResponse> handleWeakPassword(WeakPasswordException ex){
    ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, System.currentTimeMillis());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
      HttpStatus status = HttpStatus.resolve(ex.status());
      if (status == null) {
          status = HttpStatus.INTERNAL_SERVER_ERROR;
      }

      String responseBody = ex.contentUTF8(); // raw JSON from User Service
      ObjectMapper mapper = new ObjectMapper();

      try {
          // try to parse the downstream response into ErrorResponse
          ErrorResponse error = mapper.readValue(responseBody, ErrorResponse.class);
          return ResponseEntity.status(status).body(error);
      } catch (Exception e) {
          // fallback if response is not JSON
          ErrorResponse error = new ErrorResponse(
                  ex.getMessage(),
                  status.value(),
                  System.currentTimeMillis()
          );
          return ResponseEntity.status(status).body(error);
      }
  }
}
