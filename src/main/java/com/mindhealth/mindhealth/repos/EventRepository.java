package com.mindhealth.mindhealth.repos;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findFirstByCategory(Category category);
    Event findFirstByOrganizer(User user);
    List<Event> findTop6ByOrderByDateCreatedDesc();
}
