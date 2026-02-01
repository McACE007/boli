package com.boli.common.enums;

public enum RoleType {
  USER, ADMIN;

  public String asAuthority() {
    return "ROLE_" + this.name();
  }
}
