package com.mindhealth.mindhealth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final int MIN_KEY_BYTES = 32; // 256 bits for HS256

    private final javax.crypto.SecretKey key;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:change-me-in-production-use-at-least-32-chars}") String secret,
            @Value("${jwt.expiration:86400000}") long expirationMs) {
        try {
            this.key = buildHmacKey(secret.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Failed to initialize JWT key", e);
        }
        this.expirationMs = expirationMs;
    }

    private javax.crypto.SecretKey buildHmacKey(byte[] secretBytes) throws NoSuchAlgorithmException, InvalidKeyException {
        if (secretBytes.length >= MIN_KEY_BYTES) {
            return Keys.hmacShaKeyFor(secretBytes);
        }
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretBytes, "HmacSHA256"));
        byte[] derivedKey = mac.doFinal("mindhealth-jwt-key".getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(derivedKey);
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
