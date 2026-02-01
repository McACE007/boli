package com.boli.userservice.repository.wrapper;

import org.springframework.stereotype.Repository;

import com.boli.common.exception.UserNotFoundException;
import com.boli.userservice.model.User;
import com.boli.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryWrapper {
  private final UserRepository userRepository;

  public User getByUsername(String username) {
    log.debug("data_user_fetch_by_username_started | username={}", username);

    User user = userRepository.findByUsername(username).orElseThrow(() -> {
      log.warn("data_user_fetch_by_username_failed | reason=user_not_found | username={}", username);
      return new UserNotFoundException(username);
    });

    return user;
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
