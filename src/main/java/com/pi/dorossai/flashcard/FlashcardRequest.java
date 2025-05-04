package com.pi.dorossai.flashcard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardRequest {
    @NotBlank
    private String topic;
    
    @Min(1)
    @Max(20)
    private int numCards = 10;
}