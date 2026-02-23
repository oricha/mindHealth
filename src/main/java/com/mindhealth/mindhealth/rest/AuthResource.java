package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.security.JwtTokenProvider;
import com.mindhealth.mindhealth.util.PasswordPolicy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {
    private final JwtTokenProvider jwt;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    @Data
    public static class LoginRequest { private String username; private String password; }
    @Data
    public static class RefreshRequest { private String token; }
    @Data
    public static class PasswordResetStartRequest { private String email; }
    @Data
    public static class PasswordResetRequest { private String token; private String newPassword; }
    @Data
    public static class TokenResponse { private final String accessToken; }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        User user = users.findByEmail(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (user.getPassword() == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return ResponseEntity.ok(new TokenResponse(jwt.generateToken(user.getEmail())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        if (!jwt.validateToken(request.getToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        String subject = jwt.getSubject(request.getToken());
        return ResponseEntity.ok(new TokenResponse(jwt.generateToken(subject)));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> startReset(@RequestBody PasswordResetStartRequest request) {
        User user = users.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, user.getEmail());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        String email = resetTokens.remove(request.getToken());
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reset token");
        }
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        PasswordPolicy.validate(request.getNewPassword());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        users.save(user);
        return ResponseEntity.ok().build();
    }
}
