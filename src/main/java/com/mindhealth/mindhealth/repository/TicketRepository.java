package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findFirstByUser(User user);
    Ticket findFirstByEvent(Event event);
    List<Ticket> findByUser(User user);
    List<Ticket> findByUser_Id(Long userId);
    Ticket findByQrCode(String qrCode);
}
