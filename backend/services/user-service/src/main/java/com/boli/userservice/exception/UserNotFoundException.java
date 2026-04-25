package com.boli.userservice.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String identifier) {
    super("User not found: " + identifier);
  }
}
