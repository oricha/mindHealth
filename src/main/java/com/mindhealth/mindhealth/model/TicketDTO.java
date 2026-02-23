package com.mindhealth.mindhealth.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor(force = true)
public class TicketDTO {

    private Long id;

    @Size(max = 255)
    private String qrCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime purchaseDate;

    @NotNull
    @Size(max = 255)
    private String paymentStatus;

    @NotNull
    private Long user;

    @NotNull
    private Long event;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;
}
