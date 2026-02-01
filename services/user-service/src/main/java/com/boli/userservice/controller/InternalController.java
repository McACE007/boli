package com.boli.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boli.common.dto.ApiResponse;
import com.boli.common.dto.InternalUserProfileDto;
import com.boli.userservice.service.InternalService;
import com.boli.common.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@Slf4j
public class InternalController {
  private final InternalService internalService;

  @GetMapping("/user/{username}")
  public ResponseEntity<ApiResponse<InternalUserProfileDto>> getInternalUserProfile(
      @PathVariable("username") String username) {
    log.info("user_profile_fetch_initiated_by_internal | username={}", username);
    InternalUserProfileDto internalUserProfileDto = internalService.getInternalUserProfile(username);
    log.info("user_profile_fetch_success_by_internal | username={}", username);
    return ResponseBuilder.success(internalUserProfileDto, "User Profile Fetched Successfully");
  }
}
