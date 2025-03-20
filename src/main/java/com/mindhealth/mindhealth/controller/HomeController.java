package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    @GetMapping("/home")
    public String showNewHome(Model model) {
        try {
            List<EventDTO> popularEvents = eventService.getPopularEvents();
            model.addAttribute("popularEvents", popularEvents);
            return "home/index2";
        } catch (Exception e) {
            // Log the error
            return "error";
        }
    }

}
