package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.repository.TicketRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public List<UserDTO> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userRepository.findByIdIn(ids).stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public void create(UserDTO userDTO) {
        User user = new User();
        mapToEntity(userDTO, user);
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : "ROLE_USER");
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);
    }

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

    public void update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);
    }

    public void updateProfile(Long userId, UserDTO dto) {
        User u = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        u.setName(dto.getName());
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        userRepository.save(u);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public ReferencedWarning getReferencedWarning(Long id) {
        ReferencedWarning referencedWarning = new ReferencedWarning();
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        Event organizerEvent = eventRepository.findFirstByOrganizer(user);
        if (organizerEvent != null) {
            referencedWarning.setKey("user.event.organizer.referenced");
            referencedWarning.addParam(organizerEvent.getId());
            return referencedWarning;
        }
        Ticket userTicket = ticketRepository.findFirstByUser(user);
        if (userTicket != null) {
            referencedWarning.setKey("user.ticket.user.referenced");
            referencedWarning.addParam(userTicket.getId());
            return referencedWarning;
        }
        return null;
    }

    public List<Ticket> getUserEventHistory(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return u.getUserTickets().stream().toList();
    }

    private UserDTO mapToDTO(User user, UserDTO dto) {
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setDateCreated(user.getDateCreated());
        return dto;
    }

    private void mapToEntity(UserDTO dto, User user) {
        user.setEmail(dto.getEmail());
        user.setName(dto.getName() != null ? dto.getName() : "");
        user.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : "");
        user.setLastName(dto.getLastName() != null ? dto.getLastName() : "");
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        user.setAvatarUrl(dto.getAvatarUrl());
    }
}

