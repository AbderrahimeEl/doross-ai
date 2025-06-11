package com.pi.dorossai.auth.dto;

import com.pi.dorossai.user.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User registration request containing user details")
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "User's full name", example = "John Doe", required = true, minLength = 3, maxLength = 50)
    private String name;
    
    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true, maxLength = 50)
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    @Schema(description = "User's password", example = "securePassword123", required = true, minLength = 6, maxLength = 40)
    private String password;
    
    @Schema(description = "User role", example = "USER", defaultValue = "USER", allowableValues = {"USER", "ADMIN"})
    private Role role;
}