package com.example.amsnew.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Paths to skip (swagger/openapi/static etc)
    private static final List<String> SKIP_PATHS = List.of(
        "/v3/api-docs",
        "/v3/api-docs/",
        "/v3/api-docs/",
        "/swagger-ui",
        "/swagger-ui/",
        "/swagger-ui/index.html",
        "/swagger-ui.html",
        "/swagger-resources",
        "/webjars",
        "/favicon.ico",
        "/user/register",
        "/user/login",
        "/user/getAllEmployees",
        "/user/delete/{employeeId}",
        "/attendance/login",
        "/attendance/logout/*",
        "/department/update{id}"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Skip OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // skip when the request path starts with any skip path
        for (String skip : SKIP_PATHS) {
            if (path.startsWith(skip)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    	String path = request.getServletPath();

    	for (String skip : SKIP_PATHS) {
    	    if (path.startsWith(skip)) {
    	        filterChain.doFilter(request, response);
    	        return;
    	    }
    	}
    	
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                org.springframework.security.core.userdetails.UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
