package com.pi.dorossai.flashcard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing generated flashcards")
public class FlashcardResponse {
    @Schema(description = "List of generated flashcards")
    private List<Flashcard> flashcards;
    
    @Schema(description = "Topic that was used for generation", example = "photosynthesis")
    private String topic;
    
    @Schema(description = "Number of flashcards generated", example = "5")
    private int numCards;
}