package com.pi.dorossai.writing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingImprovementRequest {
    @NotBlank
    private String text;
    
    private String style = "professional";
}
