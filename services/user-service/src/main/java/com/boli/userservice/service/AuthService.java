package com.boli.userservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.boli.userservice.dto.LoginRequest;
import com.boli.userservice.dto.RegisterRequest;
import com.boli.userservice.enums.RoleType;
import com.boli.userservice.enums.UserStatus;
import com.boli.userservice.repository.UserRepository;
import com.boli.userservice.security.JwtUtil;
import com.boli.userservice.exception.InvalidCredentialsException;
import com.boli.userservice.exception.UserAlreadyExistsException;
import com.boli.userservice.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public void register(RegisterRequest request) {

    log.info("user_registration_initiated | username={}", request.getUsername());

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      log.warn("user_registration_failed_username_exists | username={}", request.getUsername());
      throw new UserAlreadyExistsException(request.getUsername());
    }

    try {
      User user = User.builder()
          .fullName(request.getFullName())
          .username(request.getUsername())
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .status(UserStatus.ACTIVE)
          .role(RoleType.USER)
          .build();

      userRepository.save(user);

      log.info("user_registration_success | user_id={} | username={}", user.getId(), user.getUsername());
    } catch (Exception e) {
      log.error(
          "user_registration_failed_unexpected | username={}",
          request.getUsername(),
          e);
      throw e;
    }
  }

  public String login(LoginRequest request) {
    log.info("user_login_initiated | username={}", request.getUsername());

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
      log.warn("user_login_failed_user_not_found | username={}", request.getUsername());
      return new InvalidCredentialsException();
    });

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      log.warn("user_login_failed_invaild_password | username={}", request.getUsername());
      throw new InvalidCredentialsException();
    }

    String token = jwtUtil.generateToken(request.getUsername(), user.getId(), user.getRole(), user.getStatus());

    log.info("user_login_success | user_id={} | username={}", user.getId(), user.getUsername());

    return token;
  }
}
