package com.boli.userservice.service;

import org.springframework.stereotype.Service;

import com.boli.userservice.dto.UserStatusDto;
import com.boli.userservice.enums.UserStatus;
import com.boli.userservice.mapper.UserMapper;
import com.boli.userservice.model.User;
import com.boli.userservice.repository.wrapper.UserRepositoryWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
  private final UserMapper userMapper;
  private final UserRepositoryWrapper userRepositoryWrapper;

  public UserStatusDto blockUser(String username) {
    log.debug("service_admin_block_user_started | username={}", username);
    User user = userRepositoryWrapper.getByUsername(username);

    if (user.getStatus() == UserStatus.BLOCKED) {
      log.warn("service_admin_block_user_skipped | reason=user_already_blocked | username={}", username);
      return userMapper.toUserStatusDto(user);
    }

    user.setStatus(UserStatus.BLOCKED);
    user = userRepositoryWrapper.save(user);

    log.info("user_blocked_by_admin | username={}", username);

    return userMapper.toUserStatusDto(user);
  }

  public UserStatusDto unblockUser(String username) {
    log.debug("service_admin_unblock_user_started | username={}", username);
    User user = userRepositoryWrapper.getByUsername(username);

    if (user.getStatus() != UserStatus.BLOCKED) {
      log.warn("service_admin_unblock_user_skipped | reason=user_already_unblocked | username={}", username);
      return userMapper.toUserStatusDto(user);
    }

    user.setStatus(UserStatus.ACTIVE);
    user = userRepositoryWrapper.save(user);

    log.info("user_unblocked_by_admin | username={}", username);

    return userMapper.toUserStatusDto(user);
  }
}
