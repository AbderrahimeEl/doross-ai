package com.pi.dorossai.flashcard;

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
@Schema(description = "Request to generate flashcards on a specific topic")
public class FlashcardRequest {
    @NotBlank
    @Schema(description = "Topic for flashcard generation", example = "photosynthesis", required = true)
    private String topic;
    
    @Min(1)
    @Max(20)
    @Schema(description = "Number of flashcards to generate", example = "5", defaultValue = "10", minimum = "1", maximum = "20")
    private int numCards = 10;
}