package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.TicketDTO;
import com.mindhealth.mindhealth.repos.EventRepository;
import com.mindhealth.mindhealth.repos.UserRepository;
import com.mindhealth.mindhealth.service.TicketService;
import com.mindhealth.mindhealth.util.CustomCollectors;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import com.mindhealth.mindhealth.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public TicketController(final TicketService ticketService, final UserRepository userRepository,
            final EventRepository eventRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
        model.addAttribute("eventValues", eventRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Event::getId, Event::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "ticket/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ticket") final TicketDTO ticketDTO) {
        return "ticket/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ticket") @Valid final TicketDTO ticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/add";
        }
        ticketService.create(ticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ticket.create.success"));
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("ticket", ticketService.get(id));
        return "ticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("ticket") @Valid final TicketDTO ticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/edit";
        }
        ticketService.update(id, ticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ticket.update.success"));
        return "redirect:/tickets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = ticketService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            ticketService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ticket.delete.success"));
        }
        return "redirect:/tickets";
    }

}
