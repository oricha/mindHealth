package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long registerUser(UserDTO dto) {
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setName(dto.getName());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setRole("ROLE_USER");
        if (dto.getPassword() != null) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userRepository.save(u).getId();
    }

    public void updateProfile(Long userId, UserDTO dto) {
        User u = userRepository.findById(userId).orElseThrow();
        u.setName(dto.getName());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        userRepository.save(u);
    }

    public List<Ticket> getUserEventHistory(Long userId) {
        User u = userRepository.findById(userId).orElseThrow();
        return u.getUserTickets().stream().toList();
    }
}

