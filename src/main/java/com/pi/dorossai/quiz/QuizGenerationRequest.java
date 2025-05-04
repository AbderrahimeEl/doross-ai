package com.pi.dorossai.quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizGenerationRequest {
    @NotBlank
    private String topic;
    
    @Min(1)
    @Max(20)
    private int numQuestions = 5;
    
    private String difficulty = "medium";
    
    private String language = "english";
}