package com.mindhealth.mindhealth.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    public List<String> autocomplete(String prefix) {
        if (prefix == null || prefix.isBlank()) return Collections.emptyList();
        // Placeholder: implement autocomplete via repository later
        return Collections.emptyList();
    }

    public List<?> search(String query) {
        // Placeholder: implement full-text search via repository later
        return Collections.emptyList();
    }
}

