package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Notification;
import com.mindhealth.mindhealth.domain.Payment;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.UserDTO;
import com.mindhealth.mindhealth.repos.EventRepository;
import com.mindhealth.mindhealth.repos.NotificationRepository;
import com.mindhealth.mindhealth.repos.PaymentRepository;
import com.mindhealth.mindhealth.repos.TicketRepository;
import com.mindhealth.mindhealth.repos.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;

    public UserService(final UserRepository userRepository, final EventRepository eventRepository,
            final TicketRepository ticketRepository, final PaymentRepository paymentRepository,
            final NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Event organizerEvent = eventRepository.findFirstByOrganizer(user);
        if (organizerEvent != null) {
            referencedWarning.setKey("user.event.organizer.referenced");
            referencedWarning.addParam(organizerEvent.getId());
            return referencedWarning;
        }
        final Ticket userTicket = ticketRepository.findFirstByUser(user);
        if (userTicket != null) {
            referencedWarning.setKey("user.ticket.user.referenced");
            referencedWarning.addParam(userTicket.getId());
            return referencedWarning;
        }
        final Payment userPayment = paymentRepository.findFirstByUser(user);
        if (userPayment != null) {
            referencedWarning.setKey("user.payment.user.referenced");
            referencedWarning.addParam(userPayment.getId());
            return referencedWarning;
        }
        final Notification userNotification = notificationRepository.findFirstByUser(user);
        if (userNotification != null) {
            referencedWarning.setKey("user.notification.user.referenced");
            referencedWarning.addParam(userNotification.getId());
            return referencedWarning;
        }
        return null;
    }

    public List<UserDTO> getUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        
        return userRepository.findAllById(userIds)
                .stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

}
