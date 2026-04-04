package com.boli.userservice.mapper;

import org.springframework.stereotype.Component;

import com.boli.userservice.dto.UserSummary;
import com.boli.userservice.dto.UserProfileResponse;
import com.boli.userservice.dto.UserStatusDto;
import com.boli.common.enums.UserStatus;
import com.boli.userservice.model.User;

@Component
public class UserMapper {
  public UserProfileResponse toProfileDto(User user) {
    return UserProfileResponse.builder().username(user.getUsername()).fullName(user.getFullName())
        .email(user.getEmail()).role(user.getRole()).status(user.getStatus()).addresses(user.getAddresses()).build();
  }

  public UserStatusDto toUserStatusDto(User user) {
    return UserStatusDto.builder().username(user.getUsername()).status(user.getStatus()).build();
  }

  public UserSummary toUserSummary(User user) {
    return UserSummary.builder().username(user.getUsername()).role(user.getRole()).status(user.getStatus()).build();
  }
}
