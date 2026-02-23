package com.mindhealth.mindhealth.service;

import com.mindhealth.mindhealth.model.EventDTO;
import com.mindhealth.mindhealth.repository.EventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final EventRepository eventRepository;
    private final MeterRegistry meterRegistry;

    public SearchService(EventRepository eventRepository, MeterRegistry meterRegistry) {
        this.eventRepository = eventRepository;
        this.meterRegistry = meterRegistry;
    }

    @Cacheable(cacheNames = "event-autocomplete", key = "#prefix")
    public List<String> autocomplete(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return List.of();
        }
        meterRegistry.counter("search.autocomplete.requests").increment();
        return eventRepository.findAutocompleteTitles(prefix.trim().toLowerCase(), PageRequest.of(0, 10));
    }

    public List<EventDTO> search(String query, String location, Long categoryId, int page, int size) {
        meterRegistry.counter("search.fulltext.requests").increment();
        return eventRepository.search(query, location, categoryId, PageRequest.of(page, size))
                .stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    dto.setId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setDescription(event.getDescription());
                    dto.setDateTime(event.getDateTime());
                    dto.setLocation(event.getLocation());
                    dto.setPrice(event.getPrice());
                    dto.setAvailableSeats(event.getAvailableSeats());
                    dto.setImageUrl(event.getImageUrl());
                    dto.setCategory(event.getCategory() == null ? null : event.getCategory().getId());
                    dto.setOrganizer(event.getOrganizer() == null ? null : event.getOrganizer().getId());
                    dto.setOrganizerId(event.getOrganizer() == null ? null : event.getOrganizer().getId());
                    return dto;
                })
                .toList();
    }
}
