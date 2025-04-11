package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.PaymentDTO;
import com.mindhealth.mindhealth.repository.PaymentRepository;
import com.mindhealth.mindhealth.repository.TicketRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final UserRepository userRepository, final TicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    public void update(final Long id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setUser(payment.getUser() == null ? null : payment.getUser().getId());
        paymentDTO.setTicket(payment.getTicket() == null ? null : payment.getTicket().getId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setStatus(paymentDTO.getStatus());
        final User user = paymentDTO.getUser() == null ? null : userRepository.findById(paymentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        payment.setUser(user);
        final Ticket ticket = paymentDTO.getTicket() == null ? null : ticketRepository.findById(paymentDTO.getTicket())
                .orElseThrow(() -> new NotFoundException("ticket not found"));
        payment.setTicket(ticket);
        return payment;
    }

}
