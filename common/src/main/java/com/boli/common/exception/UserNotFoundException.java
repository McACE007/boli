package com.boli.common.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String identifier) {
    super("User not found: " + identifier);
  }
}
