package com.mindhealth.mindhealth;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.repos.CategoryRepository;
import com.mindhealth.mindhealth.repos.EventRepository;
import com.mindhealth.mindhealth.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MindhealthApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MindhealthApplication.class, args);
    }

    //@Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            EventRepository eventRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Users
            User admin = new User();
            admin.setEmail("admin@mindhealth.com");
            admin.setName("Admin User");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            User organizer = new User();
            organizer.setName("Organizer User");
            organizer.setEmail("organizer@mindhealth.com");
            organizer.setPassword(passwordEncoder.encode("organizer123"));
            organizer.setFirstName("John");
            organizer.setLastName("Doe");
            organizer.setRole("ROLE_ORGANIZER");
            userRepository.save(organizer);

            // Create Categories
            Category meditation = new Category();
            meditation.setName("Meditation");
            meditation.setDescription("Mindfulness and meditation sessions");
            categoryRepository.save(meditation);

            Category yoga = new Category();
            yoga.setName("Yoga");
            yoga.setDescription("Yoga classes and workshops");
            categoryRepository.save(yoga);

            Category therapy = new Category();
            therapy.setName("Therapy");
            therapy.setDescription("Mental health therapy sessions");
            categoryRepository.save(therapy);

            // Create Events
            List<Event> events = Arrays.asList(
                createEvent("Morning Meditation", "Start your day with guided meditation", 
                    "https://example.com/meditation.jpg", 
                    OffsetDateTime.now().plusDays(1), 
                    "Virtual", 
                    new BigDecimal("29.99"), 
                    50, 
                    meditation, 
                    organizer),
                createEvent("Yoga for Beginners", "Learn the basics of yoga", 
                    "https://example.com/yoga.jpg", 
                    OffsetDateTime.now().plusDays(3), 
                    "Studio A", 
                    new BigDecimal("49.99"), 
                    20, 
                    yoga, 
                    organizer),
                createEvent("Stress Management Workshop", "Learn techniques to manage stress", 
                    "https://example.com/stress.jpg", 
                    OffsetDateTime.now().plusDays(5), 
                    "Conference Room B", 
                    new BigDecimal("79.99"), 
                    30, 
                    therapy, 
                    organizer)
            );

            events.forEach(eventRepository::save);
        };
    }

    private Event createEvent(String title, String description, String imageUrl, 
                            OffsetDateTime dateTime, String location, 
                            BigDecimal price, Integer availableSeats, 
                            Category category, User organizer) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setImageUrl(imageUrl);
        event.setDateTime(dateTime);
        event.setLocation(location);
        event.setPrice(price);
        event.setAvailableSeats(availableSeats);
        event.setCategory(category);
        event.setOrganizer(organizer);
        return event;
    }
}
