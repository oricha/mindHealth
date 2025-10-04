package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.security.JwtTokenProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {
    private final JwtTokenProvider jwt;

    @Data
    public static class LoginRequest { private String username; private String password; }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        // Placeholder: validate credentials via Spring Security authentication manager in future
        String token = jwt.generateToken(req.getUsername());
        return ResponseEntity.ok(token);
    }
}

