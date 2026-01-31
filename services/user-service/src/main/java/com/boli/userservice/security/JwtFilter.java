package com.boli.userservice.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.boli.userservice.enums.RoleType;
import com.boli.userservice.enums.UserStatus;
import com.boli.userservice.security.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      log.debug(
          "jwt_auth_skipped_no_bearer | path={} | method={}",
          request.getRequestURI(),
          request.getMethod());
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    try {
      Claims claims = jwtUtil.extractClaims(token);

      String username = claims.getSubject();
      String roleStr = claims.get("role", String.class);
      String statusStr = claims.get("status", String.class);
      UserStatus status = UserStatus.valueOf(statusStr);
      RoleType role = RoleType.valueOf(roleStr);

      if (status != UserStatus.ACTIVE) {
        log.warn(
            "jwt_auth_failed_user_inactive | username={} | status={}",
            username,
            status);
        filterChain.doFilter(request, response);
        return;
      }

      var authorities = Collections.singletonList(new SimpleGrantedAuthority(role.asAuthority()));

      var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);

      log.debug(
          "jwt_auth_success | username={} | role={}",
          username,
          role);

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      log.warn(
          "jwt_auth_failed_invalid_token | reason={}",
          e.getClass().getSimpleName());
      filterChain.doFilter(request, response);
      return;
    }
    filterChain.doFilter(request, response);
  }

}
