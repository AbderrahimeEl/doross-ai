package com.pi.dorossai.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeExplanationRequest {
    @NotBlank
    private String code;
    
    private String language = "python";
    
    private String detailLevel = "intermediate";
}
