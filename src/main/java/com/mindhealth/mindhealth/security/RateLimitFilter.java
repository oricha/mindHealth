package com.mindhealth.mindhealth.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createBucket(String path) {
        Bandwidth bandwidth;
        if (path.startsWith("/api/auth")) {
            bandwidth = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        } else if (path.startsWith("/api/payments")) {
            bandwidth = Bandwidth.classic(30, Refill.greedy(30, Duration.ofMinutes(1)));
        } else if (path.startsWith("/api/events/search") || path.startsWith("/api/events/autocomplete")) {
            bandwidth = Bandwidth.classic(120, Refill.greedy(120, Duration.ofMinutes(1)));
        } else {
            bandwidth = Bandwidth.classic(200, Refill.greedy(200, Duration.ofMinutes(1)));
        }
        return Bucket.builder().addLimit(bandwidth).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String key = request.getRemoteAddr() + ":" + path;
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(path));

        if (!bucket.tryConsume(1)) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"rate_limit_exceeded\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
