package com.pi.dorossai.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "email")
       })
@Schema(description = "User entity representing system users with authentication and role information")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Unique identifier for the user",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Schema(
        description = "User's full name",
        example = "John Doe",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(
        description = "User's email address (unique)",
        example = "john.doe@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    private String email;

    @NotBlank
    @Size(max = 120)
    @Schema(
        description = "User's encrypted password",
        example = "securePassword123",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.WRITE_ONLY,
        maxLength = 120
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(
        description = "User's role in the system",
        example = "USER",
        allowableValues = {"USER", "ADMIN"},
        defaultValue = "USER"
    )
    private Role role;
}