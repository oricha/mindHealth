package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.domain.Category;
import com.mindhealth.mindhealth.domain.Event;
import com.mindhealth.mindhealth.domain.Ticket;
import com.mindhealth.mindhealth.domain.User;
import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.repos.CategoryRepository;
import com.mindhealth.mindhealth.repos.EventRepository;
import com.mindhealth.mindhealth.repos.TicketRepository;
import com.mindhealth.mindhealth.repos.UserRepository;
import com.mindhealth.mindhealth.util.NotFoundException;
import com.mindhealth.mindhealth.util.ReferencedWarning;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public EventService(final EventRepository eventRepository,
            final CategoryRepository categoryRepository, final UserRepository userRepository,
            final TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<EventDTO> findAll() {
        final List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(event -> mapToDTO(event, new EventDTO()))
                .toList();
    }

    public EventDTO get(final Long id) {
        return eventRepository.findById(id)
                .map(event -> mapToDTO(event, new EventDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EventDTO eventDTO) {
        final Event event = new Event();
        mapToEntity(eventDTO, event);
        return eventRepository.save(event).getId();
    }

    public void update(final Long id, final EventDTO eventDTO) {
        final Event event = eventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventDTO, event);
        eventRepository.save(event);
    }


    public List<EventDTO> getPopularEvents() {
        // This is a basic implementation. You might want to modify the logic
        // based on your specific criteria for "popular" events
        return eventRepository.findTop6ByOrderByDateCreatedDesc()
                .stream()
                .map(event -> mapToDTO(event, new EventDTO()))
                .toList();
    }


    public void delete(final Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO mapToDTO(final Event event, final EventDTO eventDTO) {
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDateTime(event.getDateTime());
        eventDTO.setLocation(event.getLocation());
        eventDTO.setPrice(event.getPrice());
        eventDTO.setAvailableSeats(event.getAvailableSeats());
        eventDTO.setCategory(event.getCategory() == null ? null : event.getCategory().getId());
        eventDTO.setOrganizer(event.getOrganizer() == null ? null : event.getOrganizer().getId());
        return eventDTO;
    }

    private Event mapToEntity(final EventDTO eventDTO, final Event event) {
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());
        event.setLocation(eventDTO.getLocation());
        event.setPrice(eventDTO.getPrice());
        event.setAvailableSeats(eventDTO.getAvailableSeats());
        final Category category = eventDTO.getCategory() == null ? null : categoryRepository.findById(eventDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        event.setCategory(category);
        final User organizer = eventDTO.getOrganizer() == null ? null : userRepository.findById(eventDTO.getOrganizer())
                .orElseThrow(() -> new NotFoundException("organizer not found"));
        event.setOrganizer(organizer);
        return event;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Event event = eventRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Ticket eventTicket = ticketRepository.findFirstByEvent(event);
        if (eventTicket != null) {
            referencedWarning.setKey("event.ticket.event.referenced");
            referencedWarning.addParam(eventTicket.getId());
            return referencedWarning;
        }
        return null;
    }

}
