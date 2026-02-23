package com.mindhealth.mindhealth.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor(force = true)
public class CategoryDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;
}
