package com.boli.auctionservice.exception;

import java.util.HashMap;
import java.util.Map;

import com.boli.common.exception.InvalidCredentialsException;
import com.boli.common.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.boli.common.dto.ApiResponse;
import com.boli.common.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvaidCredentialsException(InvalidCredentialsException e) {
    return ResponseBuilder.unauthorized(e.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    return ResponseBuilder.conflict(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    log.warn("request_validation_failed | errors={}", errors);

    return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Validation failed", errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("unexcepted_exception | error_message={}", e.getMessage(), e);
    return ResponseBuilder.internalServerError("Internal Server Error");
  }
}
