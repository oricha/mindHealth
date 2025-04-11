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
import com.mindhealth.mindhealth.util.ReferencedWarning;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;

    public TicketService(final TicketRepository ticketRepository,
            final UserRepository userRepository, final EventRepository eventRepository,
            final PaymentRepository paymentRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<TicketDTO> findAll() {
        final List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .toList();
    }

    public TicketDTO get(final Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> mapToDTO(ticket, new TicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);
        return ticketRepository.save(ticket).getId();
    }

    public void update(final Long id, final TicketDTO ticketDTO) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ticketDTO, ticket);
        ticketRepository.save(ticket);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket, final TicketDTO ticketDTO) {
        ticketDTO.setId(ticket.getId());
        ticketDTO.setQrCode(ticket.getQrCode());
        ticketDTO.setPurchaseDate(ticket.getPurchaseDate());
        ticketDTO.setPaymentStatus(ticket.getPaymentStatus());
        ticketDTO.setUser(ticket.getUser() == null ? null : ticket.getUser().getId());
        ticketDTO.setEvent(ticket.getEvent() == null ? null : ticket.getEvent().getId());
        return ticketDTO;
    }

    private Ticket mapToEntity(final TicketDTO ticketDTO, final Ticket ticket) {
        ticket.setQrCode(ticketDTO.getQrCode());
        ticket.setPurchaseDate(ticketDTO.getPurchaseDate());
        ticket.setPaymentStatus(ticketDTO.getPaymentStatus());
        final User user = ticketDTO.getUser() == null ? null : userRepository.findById(ticketDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        ticket.setUser(user);
        final Event event = ticketDTO.getEvent() == null ? null : eventRepository.findById(ticketDTO.getEvent())
                .orElseThrow(() -> new NotFoundException("event not found"));
        ticket.setEvent(event);
        return ticket;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Payment ticketPayment = paymentRepository.findFirstByTicket(ticket);
        if (ticketPayment != null) {
            referencedWarning.setKey("ticket.payment.ticket.referenced");
            referencedWarning.addParam(ticketPayment.getId());
            return referencedWarning;
        }
        return null;
    }

}
