package com.boli.common.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.boli.common.dto.ApiResponse;

public class ResponseBuilder {
  private ResponseBuilder() {
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(true)
            .statusCode(HttpStatus.OK.value())
            .message(message)
            .data(data)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(true)
            .statusCode(HttpStatus.CREATED.value())
            .message(message)
            .data(data)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(true)
            .statusCode(HttpStatus.CREATED.value())
            .message(message)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(
            ApiResponse.<T>builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .data(null)
                .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(false)
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .message(message)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(false)
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(message)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
    return ResponseEntity.ok(
        ApiResponse.<T>builder()
            .success(false)
            .statusCode(HttpStatus.CONFLICT.value())
            .message(message)
            .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message,
      Map<String, String> errors) {
    return ResponseEntity.status(status)
        .body(
            ApiResponse.<T>builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .errors(errors)
                .build());
  }
}
