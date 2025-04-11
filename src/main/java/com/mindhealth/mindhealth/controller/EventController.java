package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.repository.CategoryRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.service.EventService;
import com.mindhealth.mindhealth.service.UserService;
import com.mindhealth.mindhealth.util.CustomCollectors;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import com.mindhealth.mindhealth.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.OffsetDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public EventController(final EventService eventService,
            final CategoryRepository categoryRepository, final UserRepository userRepository, final UserService userService) {
        this.eventService = eventService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("categoryValues", categoryRepository.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(Category::getId, Category::getName)));
        model.addAttribute("organizerValues", userRepository.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
    }

    @GetMapping
    public String listEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "date") String sort,
            Model model
    ) {
        // Get events with filters
        List<EventDTO> events = eventService.findEvents(search, category, date, location, sort);
        
        // Add to model
        model.addAttribute("events", events);
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("currentSort", sort);
        
        return "events/eventlist";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("event") final EventDTO eventDTO) {
        return "event/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("event") @Valid final EventDTO eventDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/add";
        }
        eventService.create(eventDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("event.create.success"));
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("event", eventService.get(id));
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("event") @Valid final EventDTO eventDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "event/edit";
        }
        eventService.update(id, eventDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("event.update.success"));
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = eventService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            eventService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("event.delete.success"));
        }
        return "redirect:/events";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute("event") EventDTO eventDTO, @RequestParam("dateTimeOffset") String offset) {
        // Combine the datetime-local value with the selected offset
        String dateTimeWithOffset = eventDTO.getDateTime().toString() + offset;
        eventDTO.setDateTime(OffsetDateTime.parse(dateTimeWithOffset));
        eventService.create(eventDTO);
        return "redirect:/events";
    }

    @GetMapping("/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        try {
            // Obtener los detalles del evento
            EventDTO event = eventService.getEventById(id);
            
            // Obtener la información del organizador
            UserDTO organizer = userService.getUserById(event.getOrganizerId());
            
            // Obtener eventos relacionados del mismo organizador
            List<EventDTO> relatedEvents = eventService.findByOrganizer(event.getOrganizerId(), id);
            
            // Agregar los atributos al modelo
            model.addAttribute("event", event);
            model.addAttribute("organizer", organizer);
            model.addAttribute("relatedEvents", relatedEvents);
            
            return "eventdetail/eventdetail";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return "error";
        }
    }

}
