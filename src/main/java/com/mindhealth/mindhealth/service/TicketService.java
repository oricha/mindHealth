package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.repository.EventRepository;
import com.mindhealth.mindhealth.repository.TicketRepository;
import com.mindhealth.mindhealth.util.QRCodeGenerator;
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
}

