package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Notification;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findFirstByUser(User user);
}
