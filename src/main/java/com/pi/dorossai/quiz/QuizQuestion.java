package com.pi.dorossai.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "A single quiz question with multiple choice options")
public class QuizQuestion {
    @Schema(description = "The question text", example = "Which programming language is known for its use in data science?")
    private String question;
    
    @Schema(description = "List of answer options", example = "[\"Java\", \"Python\", \"C++\", \"JavaScript\"]")
    private List<String> options;
    
    @Schema(description = "The correct answer", example = "Python")
    private String correctAnswer;
    
    @Schema(description = "Explanation of why the answer is correct", example = "Python is widely used in data science due to its extensive libraries like NumPy and Pandas.")
    private String explanation;
}