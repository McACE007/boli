package com.boli.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boli.common.enums.RoleType;
import com.boli.common.enums.UserStatus;

import javax.crypto.SecretKey;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

  private final SecretKey secretKey;
  private final long expiration;

  public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expiration) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    this.expiration = expiration;

    log.info(
        "jwt_util_initialized | expiration_ms={}",
        expiration);
  }

  public String generateToken(String username, Long userId, RoleType role, UserStatus status) {
    log.debug(
        "jwt_token_generation_started | username={} | role={} | status={}",
        username,
        role,
        status);

    String token = Jwts.builder()
        .subject(username)
        .claim("userId", userId)
        .claim("role", role.name())
        .claim("status", status.name())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(secretKey)
        .compact();

    log.debug(
        "jwt_token_generation_success | username={} | expires_in_ms={}",
        username,
        expiration);

    return token;
  }

  public Claims extractClaims(String token) {
    try {
      return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    } catch (Exception e) {
      log.warn(
          "jwt_claims_extraction_failed | reason={}",
          e.getClass().getSimpleName());
      throw e;
    }
  }
}
