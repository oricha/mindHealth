package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.service.SearchService;
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
    public ResponseEntity<List<?>> search(@RequestParam("q") String q) {
        return ResponseEntity.ok(searchService.search(q));
    }
}

