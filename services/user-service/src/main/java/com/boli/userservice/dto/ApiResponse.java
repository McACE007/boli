package com.boli.userservice.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private boolean success;
  private int statusCode;
  private String message;
  private T data;
  private Map<String, String> errors;
}
