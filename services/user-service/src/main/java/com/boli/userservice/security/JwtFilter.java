package com.boli.userservice.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.boli.userservice.enums.RoleType;
import com.boli.userservice.enums.UserStatus;
import com.boli.userservice.model.Role;
import com.boli.userservice.security.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    try {
      Claims claims = jwtUtil.extractClaims(token);

      String username = claims.getSubject();
      @SuppressWarnings("unchecked")
      List<RoleType> roles = claims.get("roles", List.class);
      String statusStr = claims.get("status", String.class);
      UserStatus status = UserStatus.valueOf(statusStr);

      if (status != UserStatus.ACTIVE) {
        filterChain.doFilter(request, response);
        return;
      }

      var authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.asAuthority()))
          .toList();

      var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      filterChain.doFilter(request, response);
    }
  }

}
