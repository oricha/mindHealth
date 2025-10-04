package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Review;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEvent(Event event);
    boolean existsByUserAndEvent(User user, Event event);
}

