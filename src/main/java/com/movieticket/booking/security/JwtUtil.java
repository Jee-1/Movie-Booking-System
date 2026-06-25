package com.movieticket.booking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    // secret + expiration come from application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // CREATE a signed token (called at login)
    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(email)              // payload: who the token is about
                .claim("role", role)         // payload: custom field "role"
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)               // sign with our secret -> tamper-proof
                .compact();                  // build the final "xxx.yyy.zzz" string
    }

    // READ the email out of a token
    public String extractEmail(String token) {
        return parse(token).getSubject();
    }

    // READ the role out of a token
    public String extractRole(String token) {
        return parse(token).get("role", String.class);
    }

    // VALIDATE: signature correct AND not expired?
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // verify signature + return the claims (data inside the token)
    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)    // throws if signature wrong OR expired
                .getPayload();
    }
}
