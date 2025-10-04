package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Review;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.repository.ReviewRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewResource {
    private final ReviewRepository reviews;
    private final EventRepository events;
    private final UserRepository users;

    @Data
    public static class ReviewDTO { public Long eventId; public Long userId; public Integer rating; public String comment; }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ReviewDTO dto) {
        Event e = events.findById(dto.eventId).orElseThrow();
        User u = users.findById(dto.userId).orElseThrow();
        Review r = new Review();
        r.setEvent(e);
        r.setUser(u);
        r.setRating(dto.rating);
        r.setComment(dto.comment);
        return ResponseEntity.ok(reviews.save(r).getId());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Review>> byEvent(@PathVariable Long eventId) {
        Event e = events.findById(eventId).orElseThrow();
        return ResponseEntity.ok(reviews.findByEvent(e));
    }
}

