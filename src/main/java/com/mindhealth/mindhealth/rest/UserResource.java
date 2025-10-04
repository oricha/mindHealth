package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody UserDTO dto) {
        Long id = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        userService.updateProfile(id, dto);
        return ResponseEntity.ok().build();
    }
}

