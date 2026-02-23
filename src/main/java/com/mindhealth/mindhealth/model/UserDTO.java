package com.mindhealth.mindhealth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor(force = true)
public class UserDTO {

    private Long id;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    private String password;

    @Size(max = 255)
    private String role;

    private String avatarUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dateCreated;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
