package com.boli.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boli.common.dto.ApiResponse;
import com.boli.userservice.dto.UserProfileResponse;
import com.boli.userservice.service.UserService;
import com.boli.common.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/api/me")
  public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile(Authentication authentication) {
    String username = authentication.getName();
    log.info("api_user_profile_fetch_initiated | username={}", username);
    UserProfileResponse userProfileResponse = userService.getCurrentUserProfile(username);
    log.info("api_user_profile_fetch_success | username={}", username);
    return ResponseBuilder.success(userProfileResponse, "User Profile Fetched Successfully");
  }
}
