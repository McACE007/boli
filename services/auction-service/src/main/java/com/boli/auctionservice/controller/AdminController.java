package com.boli.auctionservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boli.common.dto.ApiResponse;
import com.boli.common.dto.UserStatusDto;
import com.boli.userservice.service.AdminService;
import com.boli.common.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
  private final AdminService adminService;

  @PostMapping("/users/{username}/block")
  public ResponseEntity<ApiResponse<UserStatusDto>> blockUser(@PathVariable("username") String username) {
    log.info("api_admin_block_user_initiated | username={}", username);
    UserStatusDto userStatusDto = adminService.blockUser(username);
    log.info("api_admin_block_user_success | username={}", username);
    return ResponseBuilder.success(userStatusDto, "User Blocked Successfully");
  }

  @PostMapping("/users/{username}/unblock")
  public ResponseEntity<ApiResponse<UserStatusDto>> unblockUser(@PathVariable("username") String username) {
    log.info("api_admin_unblock_user_initiated | username={}", username);
    UserStatusDto userStatusDto = adminService.unblockUser(username);
    log.info("api_admin_unblock_user_success | username={}", username);
    return ResponseBuilder.success(userStatusDto, "User Unblocked Successfully");
  }
}
