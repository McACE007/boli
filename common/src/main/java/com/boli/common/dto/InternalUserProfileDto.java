package com.boli.common.dto;

import com.boli.common.enums.RoleType;
import com.boli.common.enums.UserStatus;

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
