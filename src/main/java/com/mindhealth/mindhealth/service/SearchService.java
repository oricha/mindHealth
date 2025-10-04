package com.mindhealth.mindhealth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StringRedisTemplate redisTemplate;

    public List<String> autocomplete(String prefix) {
        if (prefix == null || prefix.isBlank()) return Collections.emptyList();
        // Placeholder: wire Redis-backed suggestions later
        return Collections.emptyList();
    }

    public List<?> search(String query) {
        // Placeholder: implement full-text search via repository later
        return Collections.emptyList();
    }
}

