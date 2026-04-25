package com.boli.auctionservice.exception;

import com.boli.common.dto.ApiResponse;
import com.boli.common.util.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

  @ExceptionHandler(AuctionServiceException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuctionServiceExceptions(AuctionServiceException e){
    return ResponseBuilder.error(e.getStatus(), e.getMessage(), null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    log.warn("request_validation_failed | errors={}", errors);
    return ResponseBuilder.error(HttpStatus.BAD_REQUEST,"Validation failed", errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("unexcepted_exception | error_message={}", e.getMessage(), e);
    return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", null);
  }
}
