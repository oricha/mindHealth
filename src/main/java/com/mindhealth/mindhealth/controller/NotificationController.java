package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.NotificationDTO;
import com.mindhealth.mindhealth.repos.UserRepository;
import com.mindhealth.mindhealth.service.NotificationService;
import com.mindhealth.mindhealth.util.CustomCollectors;
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
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(final NotificationService notificationService,
            final UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("notifications", notificationService.findAll());
        return "notification/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("notification") final NotificationDTO notificationDTO) {
        return "notification/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("notification") @Valid final NotificationDTO notificationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notification/add";
        }
        notificationService.create(notificationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notification.create.success"));
        return "redirect:/notifications";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final  Long id, final Model model) {
        model.addAttribute("notification", notificationService.get(id));
        return "notification/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("notification") @Valid final NotificationDTO notificationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notification/edit";
        }
        notificationService.update(id, notificationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notification.update.success"));
        return "redirect:/notifications";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        notificationService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("notification.delete.success"));
        return "redirect:/notifications";
    }

}
