package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.PaymentDTO;
import com.mindhealth.mindhealth.repository.PaymentRepository;
import com.mindhealth.mindhealth.repository.TicketRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.api-key:}")
    private String apiKey;

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public void create(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void update(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    public String createPaymentIntent(long amountCents, String currency) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountCents)
                .setCurrency(currency)
                .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }

    public boolean confirmPayment(String paymentIntentId) {
        // Placeholder: Real confirmation is from client + webhook
        return true;
    }

    private PaymentDTO mapToDTO(Payment payment, PaymentDTO dto) {
        dto.setId(payment.getId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setAmount(payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO);
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setStatus(payment.getStatus());
        dto.setUser(payment.getUser() == null ? null : payment.getUser().getId());
        dto.setTicket(payment.getTicket() == null ? null : payment.getTicket().getId());
        dto.setCreatedAt(payment.getDateCreated());
        return dto;
    }

    private void mapToEntity(PaymentDTO dto, Payment payment) {
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setStatus(dto.getStatus());
        if (dto.getUser() != null) {
            User user = userRepository.findById(dto.getUser())
                    .orElseThrow(() -> new NotFoundException("user not found"));
            payment.setUser(user);
        }
        if (dto.getTicket() != null) {
            Ticket ticket = ticketRepository.findById(dto.getTicket())
                    .orElseThrow(() -> new NotFoundException("ticket not found"));
            payment.setTicket(ticket);
        }
    }
}

