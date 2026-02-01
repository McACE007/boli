package com.boli.userservice.mapper;

import org.springframework.stereotype.Component;

import com.boli.common.dto.InternalUserProfileDto;
import com.boli.userservice.dto.UserProfileDto;
import com.boli.common.dto.UserStatusDto;
import com.boli.common.enums.UserStatus;
import com.boli.userservice.model.User;

@Component
public class UserMapper {
  public UserProfileDto toProfileDto(User user) {
    return UserProfileDto.builder().username(user.getUsername()).fullName(user.getFullName())
        .email(user.getEmail()).role(user.getRole()).status(user.getStatus()).addresses(user.getAddresses()).build();
  }

  public UserStatusDto toUserStatusDto(User user) {
    return UserStatusDto.builder().username(user.getUsername()).status(user.getStatus()).build();
  }

  public InternalUserProfileDto toInternalUserProfileDto(User user) {
    return InternalUserProfileDto.builder().username(user.getUsername()).role(user.getRole()).status(user.getStatus())
        .isBlocked(user.getStatus() == UserStatus.BLOCKED).build();
  }
}
