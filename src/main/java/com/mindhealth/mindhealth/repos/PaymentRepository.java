package com.mindhealth.mindhealth.repos;

import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findFirstByUser(User user);
    Payment findFirstByTicket(Ticket ticket);
}
