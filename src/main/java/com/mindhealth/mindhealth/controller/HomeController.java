package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.service.CategoryService;
import com.mindhealth.mindhealth.service.EventService;
import com.mindhealth.mindhealth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    public HomeController(final EventService eventService, 
                         final UserService userService,
                         final CategoryService categoryService) {
        this.eventService = eventService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/home")
    public String showNewHome(Model model) {
        try {
            // Get popular events
            List<EventDTO> popularEvents = eventService.getPopularEvents();
            
            // Get organizers for these events
            List<Long> organizerIds = popularEvents.stream()
                    .map(EventDTO::getOrganizer)
                    .distinct()
                    .collect(Collectors.toList());
            
            Map<Long, UserDTO> organizers = userService.getUsersByIds(organizerIds)
                    .stream()
                    .collect(Collectors.toMap(
                        UserDTO::getId,
                        user -> user
                    ));

            // Get categories with counts
            List<Map<String, Object>> themes = Arrays.asList(
                Map.of(
                    "name", "Workshops",
                    "count", "5,000",
                    "image", "/images/workshops.jpg"
                ),
                Map.of(
                    "name", "Ceremonies",
                    "count", "2,500",
                    "image", "/images/ceremonies.jpg"
                ),
                Map.of(
                    "name", "Dance",
                    "count", "1,477",
                    "image", "/images/dance.jpg"
                )
            );

            // Add community stories
            List<Map<String, Object>> communityStories = Arrays.asList(
                Map.of(
                    "title", "The Wise Way",
                    "description", "A creation from the heart & a place to feel, celebrate, and connect",
                    "image", "/images/community-story-1.jpg",
                    "author", Map.of(
                        "name", "Jasmine Love",
                        "role", "Organizer",
                        "avatar", "/images/avatar.jpg"
                    )
                ),
                Map.of(
                    "title", "Living down concert",
                    "description", "Is it something for you?",
                    "image", "/images/community-story-2.jpg",
                    "author", Map.of(
                        "name", "Sarah Smith",
                        "role", "Organizer",
                        "avatar", "/images/avatar2.jpg"
                    )
                )
            );

            model.addAttribute("popularEvents", popularEvents);
            model.addAttribute("organizers", organizers);
            model.addAttribute("themes", themes);
            model.addAttribute("communityStories", communityStories);
            
            return "home/index2";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Replace with proper logging
            return "error";
        }
    }
}
