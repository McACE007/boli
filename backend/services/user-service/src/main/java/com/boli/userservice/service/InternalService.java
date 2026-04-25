package com.boli.userservice.service;

import org.springframework.stereotype.Service;

import com.boli.userservice.dto.UserSummary;
import com.boli.userservice.mapper.UserMapper;
import com.boli.userservice.model.User;
import com.boli.userservice.repository.wrapper.UserRepositoryWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalService {
  private final UserMapper userMapper;
  private final UserRepositoryWrapper userRepositoryWrapper;

  public UserSummary getUserSummary(String username) {
    User user = userRepositoryWrapper.getByUsername(username);
    return userMapper.toUserSummary(user);
  }
}
