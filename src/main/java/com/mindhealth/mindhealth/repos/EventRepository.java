package com.mindhealth.mindhealth.repos;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findFirstByCategory(Category category);
    Event findFirstByOrganizer(User user);
    List<Event> findTop6ByOrderByDateCreatedDesc();
    List<Event> findByOrganizer_Id(Long organizerId);
}
