package com.pi.dorossai.writing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing improved text with detailed changes")
public class WritingImprovementResponse {
    @Schema(
        description = "The improved text after applying the requested style",
        example = "Artificial intelligence represents a transformative technology that is fundamentally reshaping industries across the globe. Through sophisticated machine learning algorithms and neural networks, AI systems can now perform complex tasks that were previously exclusive to human cognition, including pattern recognition, natural language processing, and predictive analytics."
    )
    private String improvedText;
    
    @Schema(
        description = "List of specific changes made to improve the text",
        example = "[\"Enhanced vocabulary with more precise terminology\", \"Improved sentence structure for better flow\", \"Added transitional phrases for coherence\", \"Converted to professional tone\", \"Corrected grammatical inconsistencies\"]"
    )
    private List<String> changes;
    
    @Schema(
        description = "Character count of the original text",
        example = "187"
    )
    private int originalLength;
    
    @Schema(
        description = "Character count of the improved text",
        example = "321"
    )
    private int improvedLength;
}
