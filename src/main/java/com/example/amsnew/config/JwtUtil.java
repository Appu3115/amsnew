package com.example.amsnew.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // =========================
    // Generate token (username only)
    // =========================
    public String generateToken(String username) {
        return generateToken(username, Map.of());
    }

    // =========================
    // Generate token (with claims)
    // =========================
    public String generateToken(String username, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // Generate token with role
    // =========================
    public String generateTokenWithRole(String username, String role) {
        return generateToken(username, Map.of(
            "role", "ROLE_" + role.toUpperCase()
        ));
    }

    // =========================
    // Extract username
    // =========================
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    // =========================
    // Extract role
    // =========================
    public String getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // =========================
    // Validate token
    // =========================
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // =========================
    // Internal claim parsing
    // =========================
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }
}
