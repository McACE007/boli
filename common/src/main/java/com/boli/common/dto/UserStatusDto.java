package com.boli.common.dto;

import com.boli.common.enums.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatusDto {
  private String username;
  private UserStatus status;
}
