package com.pi.dorossai.summarization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummarizationRequest {
    @NotBlank
    private String text;
    
    @NotBlank
    private String language;
}