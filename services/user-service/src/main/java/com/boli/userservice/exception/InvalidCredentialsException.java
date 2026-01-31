package com.boli.userservice.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invaild username or password");
  }
}
