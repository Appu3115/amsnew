package com.example.amsnew.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.amsnew.config.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /* ===== PUBLIC ENDPOINTS (NO JWT REQUIRED) ===== */
    private static final List<String> SKIP_PATHS = List.of(
        "/user/login",
        "/user/register",
        "/v3/api-docs",
        "/v3/api-docs/",
        "/swagger-ui",
        "/swagger-ui/",
        "/swagger-ui.html"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Skip CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return SKIP_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);

                /* ===== 🔒 ROLE NORMALIZATION (CRITICAL FIX) ===== */
                if (role != null && !role.startsWith("ROLE_")) {
                    role = "ROLE_" + role.toUpperCase();
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                /* ===== DEBUG LOGS (KEEP DURING DEV) ===== */
                System.out.println("JWT FILTER HIT → " + request.getRequestURI());
                System.out.println("AUTH USER → " + username);
                System.out.println("AUTHORITIES → " + authentication.getAuthorities());
            }
        }

        filterChain.doFilter(request, response);
    }
}
