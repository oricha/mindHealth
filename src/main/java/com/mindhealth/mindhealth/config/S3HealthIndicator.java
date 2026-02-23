package com.mindhealth.mindhealth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("s3")
public class S3HealthIndicator implements HealthIndicator {

    @Value("${aws.s3.bucket:}")
    private String bucket;

    @Override
    public Health health() {
        if (bucket == null || bucket.isBlank()) {
            return Health.unknown().withDetail("reason", "S3 bucket is not configured").build();
        }
        return Health.up().withDetail("bucket", bucket).build();
    }
}
