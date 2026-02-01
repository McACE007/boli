package com.boli.auctionservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
  @NotBlank(message = "Username is required")
  private String username;
  @NotBlank(message = "FullName is required")
  private String fullName;
  @Email
  @NotBlank(message = "Email is required")
  private String email;
  @Size(min = 8, message = "Password must be atleast 8 charactors long")
  private String password;
}
