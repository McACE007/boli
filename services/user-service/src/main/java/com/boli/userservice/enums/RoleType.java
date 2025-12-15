package com.boli.userservice.enums;

public enum RoleType {
  BUYER, SELLER, ADMIN;

  public String asAuthority() {
    return "ROLE_" + this.name();
  }
}
