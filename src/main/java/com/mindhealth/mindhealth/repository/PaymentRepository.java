package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findFirstByUser(User user);
    Payment findFirstByTicket(Ticket ticket);
    List<Payment> findByUser(User user);
}
