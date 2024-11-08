package com.example.gym.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String tokenSecret;

    @Value("${jwt.lifetime}")
    private Duration tokenLifeTime;

    public String generateAccessToken(UserDetails userDetails, String email) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .toList();
        claims.put("roles", roles);
        
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + tokenLifeTime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(getSecretKey(tokenSecret))
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, String email) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .toList();
        claims.put("roles", roles);
        
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + tokenLifeTime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(getSecretKey(tokenSecret))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            this.getClaims(token, tokenSecret);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getUsernameFromAccessToken(String token) {
        return this.getUsernameFromToken(token, tokenSecret);
    }

    public String getUsernameFromRefreshToken(String token) {
        return this.getUsernameFromToken(token, tokenSecret);
    }

    public List<String> getRolesFromAccessToken(String token) {
        return this.getRolesFromToken(token, tokenSecret);
    }

    public List<String> getRolesFromRefreshToken(String token) {
        return this.getRolesFromToken(token, tokenSecret);
    }

    @SuppressWarnings("unchecked")
    private List<String> getRolesFromToken(String token, String secret) {
        return (List<String>) this.getClaims(token, secret)
                .get("roles");
    }

    private String getUsernameFromToken(String token, String secret) {
        return this.getClaims(token, secret)
                .getSubject();
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private SecretKey getSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
