package com.pi.dorossai.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User login request containing email and password")
public class LoginRequest {
    @NotBlank
    @Email
    @Schema(description = "User email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @NotBlank
    @Schema(description = "User password", example = "securePassword123", required = true)
    private String password;
}