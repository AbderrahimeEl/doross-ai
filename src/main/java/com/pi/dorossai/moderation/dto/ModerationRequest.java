package com.pi.dorossai.moderation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModerationRequest {
    @NotBlank
    private String text;
    
    private String level = "standard"; // strict, standard, lenient
}
