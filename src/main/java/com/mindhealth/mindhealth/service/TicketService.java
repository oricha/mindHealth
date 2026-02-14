package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.TicketDTO;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.repository.PaymentRepository;
import com.mindhealth.mindhealth.repository.TicketRepository;
import com.mindhealth.mindhealth.repository.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.mindhealth.mindhealth.util.QRCodeGenerator;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public List<TicketDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .toList();
    }

    public TicketDTO get(Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);
        if (ticket.getQrCode() == null || ticket.getQrCode().isBlank()) {
            ticket.setQrCode(QRCodeGenerator.generateCode());
        }
        if (ticket.getPurchaseDate() == null) {
            ticket.setPurchaseDate(OffsetDateTime.now());
        }
        if (ticket.getQuantity() == null) {
            ticket.setQuantity(1);
        }
        return ticketRepository.save(ticket).getId();
    }

    @Transactional
    public void update(Long id, TicketDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ticketDTO, ticket);
        ticketRepository.save(ticket);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    public ReferencedWarning getReferencedWarning(Long id) {
        ReferencedWarning referencedWarning = new ReferencedWarning();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Payment ticketPayment = paymentRepository.findFirstByTicket(ticket);
        if (ticketPayment != null) {
            referencedWarning.setKey("ticket.payment.ticket.referenced");
            referencedWarning.addParam(ticketPayment.getId());
            return referencedWarning;
        }
        return null;
    }

    @Transactional
    public List<Long> generateTickets(Long eventId, User user, int quantity) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Ticket t = new Ticket();
            t.setEvent(event);
            t.setUser(user);
            t.setQuantity(1);
            t.setPaymentStatus("COMPLETED");
            t.setQrCode(QRCodeGenerator.generateCode());
            t.setPurchaseDate(OffsetDateTime.now());
            ids.add(ticketRepository.save(t).getId());
        }
        return ids;
    }

    private TicketDTO mapToDTO(Ticket ticket, TicketDTO dto) {
        dto.setId(ticket.getId());
        dto.setQrCode(ticket.getQrCode());
        dto.setPurchaseDate(ticket.getPurchaseDate());
        dto.setPaymentStatus(ticket.getPaymentStatus());
        dto.setUser(ticket.getUser() == null ? null : ticket.getUser().getId());
        dto.setEvent(ticket.getEvent() == null ? null : ticket.getEvent().getId());
        dto.setCreatedAt(ticket.getDateCreated());
        return dto;
    }

    private void mapToEntity(TicketDTO dto, Ticket ticket) {
        if (dto.getUser() != null) {
            User user = userRepository.findById(dto.getUser())
                    .orElseThrow(() -> new NotFoundException("user not found"));
            ticket.setUser(user);
        }
        if (dto.getEvent() != null) {
            Event event = eventRepository.findById(dto.getEvent())
                    .orElseThrow(() -> new NotFoundException("event not found"));
            ticket.setEvent(event);
        }
        if (dto.getQrCode() != null) {
            ticket.setQrCode(dto.getQrCode());
        }
        if (dto.getPurchaseDate() != null) {
            ticket.setPurchaseDate(dto.getPurchaseDate());
        }
        if (dto.getPaymentStatus() != null) {
            ticket.setPaymentStatus(dto.getPaymentStatus());
        }
        if (dto.getPaymentStatus() == null && ticket.getPaymentStatus() == null) {
            ticket.setPaymentStatus("PENDING");
        }
        if (ticket.getQuantity() == null) {
            ticket.setQuantity(1);
        }
    }
}

