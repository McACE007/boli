package com.boli.auctionservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boli.common.dto.ApiResponse;
import com.boli.userservice.dto.LoginRequest;
import com.boli.userservice.dto.RegisterRequest;
import com.boli.userservice.service.AuthService;
import com.boli.common.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
    log.info("auth_register_api_called | username={}", request.getUsername());
    authService.register(request);
    return ResponseBuilder.created("User Registered Successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
    log.info("auth_login_api_called | username={}", request.getUsername());
    String token = authService.login(request);
    return ResponseBuilder.success(token, "User Logged In Successfully");
  }
}
