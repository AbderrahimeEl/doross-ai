package com.pi.dorossai.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to generate a quiz on a specific topic")
public class QuizGenerationRequest {
    @NotBlank
    @Schema(description = "Topic for quiz generation", example = "artificial intelligence", required = true)
    private String topic;
    
    @Min(1)
    @Max(20)
    @Schema(description = "Number of questions to generate", example = "5", defaultValue = "5", minimum = "1", maximum = "20")
    private int numQuestions = 5;
    
    @Schema(description = "Difficulty level of the quiz", example = "medium", defaultValue = "medium", allowableValues = {"easy", "medium", "hard"})
    private String difficulty = "medium";
    
    @Schema(description = "Language for the quiz", example = "english", defaultValue = "english")
    private String language = "english";
}