package com.pi.dorossai.document.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentQARequest {
    @NotBlank
    private String question;
    
    @NotBlank
    private String context;
}
