package com.boli.userservice.service;

import org.springframework.stereotype.Service;

import com.boli.userservice.dto.UserProfileDto;
import com.boli.userservice.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
@Slf4j
public class UserService {
  private final UserRepositoryWrapper userRepositoryWrapper;
  private final UserMapper userMapper;

  public UserProfileDto getCurrentUserProfile(String username) {
    User user = userRepositoryWrapper.getByUsername(username);
    return userMapper.toProfileDto(user);
  }
}
