package com.boli.userservice.dto;

import java.util.List;

import com.boli.userservice.enums.RoleType;
import com.boli.userservice.enums.UserStatus;
import com.boli.userservice.model.Address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
  private String username;
  private String fullName;
  private String email;
  private RoleType role;
  private UserStatus status;
  private List<Address> addresses;
}
