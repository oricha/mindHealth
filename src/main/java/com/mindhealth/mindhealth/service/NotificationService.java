package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Notification;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.NotificationDTO;
import com.mindhealth.mindhealth.repository.NotificationRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDTO> findAll() {
        return notificationRepository.findAll().stream()
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .toList();
    }

    public NotificationDTO get(Long id) {
        return notificationRepository.findById(id)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(Long id, NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Async
    public void sendTicketEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendEventReminders() {
        // Placeholder: query upcoming events and send emails
    }

    private NotificationDTO mapToDTO(Notification notification, NotificationDTO dto) {
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setTimestamp(notification.getTimestamp());
        dto.setIsRead(notification.getIsRead());
        dto.setUser(notification.getUser() == null ? null : notification.getUser().getId());
        dto.setCreatedAt(notification.getDateCreated());
        return dto;
    }

    private void mapToEntity(NotificationDTO dto, Notification notification) {
        notification.setMessage(dto.getMessage());
        notification.setTimestamp(dto.getTimestamp());
        notification.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        if (dto.getUser() != null) {
            User user = userRepository.findById(dto.getUser())
                    .orElseThrow(() -> new NotFoundException("user not found"));
            notification.setUser(user);
        }
    }
}

