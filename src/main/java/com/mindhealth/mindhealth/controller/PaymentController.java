package com.mindhealth.mindhealth.controller;

import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.PaymentDTO;
import com.mindhealth.mindhealth.repos.TicketRepository;
import com.mindhealth.mindhealth.repos.UserRepository;
import com.mindhealth.mindhealth.service.PaymentService;
import com.mindhealth.mindhealth.util.CustomCollectors;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public PaymentController(final PaymentService paymentService,
            final UserRepository userRepository, final TicketRepository ticketRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
        model.addAttribute("ticketValues", ticketRepository.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(Ticket::getId, Ticket::getQrCode)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("payments", paymentService.findAll());
        return "payment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("payment") final PaymentDTO paymentDTO) {
        return "payment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("payment") @Valid final PaymentDTO paymentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment/add";
        }
        paymentService.create(paymentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("payment.create.success"));
        return "redirect:/payments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("payment", paymentService.get(id));
        return "payment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("payment") @Valid final PaymentDTO paymentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment/edit";
        }
        paymentService.update(id, paymentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("payment.update.success"));
        return "redirect:/payments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        paymentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("payment.delete.success"));
        return "redirect:/payments";
    }

}
