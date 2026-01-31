package com.boli.userservice.dto;

import com.boli.userservice.enums.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatusDto {
  private String username;
  private UserStatus status;
}
