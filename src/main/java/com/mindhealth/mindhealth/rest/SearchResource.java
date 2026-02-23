package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.service.SearchService;
import com.mindhealth.mindhealth.model.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class SearchResource {

    private final SearchService searchService;

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam("q") String q) {
        return ResponseEntity.ok(searchService.autocomplete(q));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> search(@RequestParam(value = "q", required = false) String q,
                                                 @RequestParam(value = "location", required = false) String location,
                                                 @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok(searchService.search(q, location, categoryId, page, size));
    }
}
