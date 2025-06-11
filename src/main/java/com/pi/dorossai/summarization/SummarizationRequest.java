package com.pi.dorossai.summarization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for text summarization with language preference")
public class SummarizationRequest {
    @NotBlank
    @Schema(
        description = "The text content to be summarized",
        example = "Artificial Intelligence (AI) is a branch of computer science that aims to create intelligent machines that work and react like humans. Some of the activities computers with artificial intelligence are designed for include: speech recognition, learning, planning, and problem solving. AI research has been highly successful in developing effective techniques for solving a wide range of problems, from game playing to medical diagnosis. The goals of AI research include reasoning, knowledge representation, planning, learning, natural language processing, perception and the ability to move and manipulate objects. General intelligence is among the field's long-term goals. Approaches include statistical methods, computational intelligence, and traditional symbolic AI.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String text;
    
    @NotBlank
    @Schema(
        description = "Target language for the summary",
        example = "english",
        allowableValues = {"english", "french", "spanish", "german", "italian", "portuguese", "chinese", "japanese", "korean", "arabic"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String language;
}