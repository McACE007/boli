package com.boli.userservice.dto;

import com.boli.common.enums.RoleType;
import com.boli.common.enums.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {
  private String username;
  private RoleType role;
  private UserStatus status;
}
