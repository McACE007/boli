package com.boli.userservice.dto;

import com.boli.userservice.enums.RoleType;
import com.boli.userservice.enums.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalUserProfileDto {
  private String username;
  private RoleType role;
  private UserStatus status;
  private Boolean isBlocked;
}
