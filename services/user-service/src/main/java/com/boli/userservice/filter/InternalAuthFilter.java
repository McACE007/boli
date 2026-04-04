package com.boli.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class InternalAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");

        if (userId == null || role == null) {
            log.debug("internal_auth_skipped | path={}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        var authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        // Store userId as principal — readable anywhere via Authentication
        var authentication = new UsernamePasswordAuthenticationToken(
                Long.parseLong(userId), null, authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("internal_auth_success | userId={} | role={}", userId, role);

        filterChain.doFilter(request, response);
    }
}
