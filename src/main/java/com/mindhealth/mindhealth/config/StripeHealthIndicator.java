package com.mindhealth.mindhealth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("stripe")
public class StripeHealthIndicator implements HealthIndicator {

    @Value("${stripe.api-key:}")
    private String apiKey;

    @Override
    public Health health() {
        if (apiKey == null || apiKey.isBlank()) {
            return Health.unknown().withDetail("reason", "Stripe API key is not configured").build();
        }
        return Health.up().build();
    }
}
