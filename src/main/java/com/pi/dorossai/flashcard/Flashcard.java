package com.pi.dorossai.flashcard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "A single flashcard with question and answer")
public class Flashcard {
    @Schema(description = "Question text for the flashcard", example = "What is photosynthesis?")
    private String question;
    
    @Schema(description = "Answer text for the flashcard", example = "Photosynthesis is the process by which plants use sunlight, water, and carbon dioxide to create oxygen and energy in the form of sugar.")
    private String answer;
}