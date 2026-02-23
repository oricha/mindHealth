package com.mindhealth.mindhealth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor(force = true)
public class NotificationDTO {

    private Long id;

    @NotNull
    private String message;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;

    @JsonProperty("isRead")
    private Boolean isRead;

    @NotNull
    private Long user;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;
}
