package com.pi.dorossai.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing generated quiz questions")
public class QuizResponse {
    @Schema(description = "List of quiz questions with answers")
    private List<QuizQuestion> quiz;
    
    @Schema(description = "Topic that was used for generation", example = "artificial intelligence")
    private String topic;
    
    @Schema(description = "Difficulty level of the quiz", example = "medium")
    private String difficulty;
    
    @Schema(description = "Language of the quiz", example = "english")
    private String language;
}