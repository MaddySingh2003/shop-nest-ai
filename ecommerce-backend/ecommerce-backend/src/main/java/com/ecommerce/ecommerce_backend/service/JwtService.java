package com.ecommerce.ecommerce_backend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "ThisIsA_VeryStrongSecretKey_ForJWT_Encryption_ChangeItLater123";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ----------- Generate Token -----------
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ----------- Extract Email -----------
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ----------- Validate Token -----------
    public boolean isValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isExpired(token);
    }

    // ----------- Expiry Helpers -----------
    public Date getExpiry(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isExpired(String token) {
        return getExpiry(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}
