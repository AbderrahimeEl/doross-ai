package com.pi.dorossai.mindmap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for generating a mind map.
 * 
 * The generated mind map will be returned in Mermaid graph TD format, which can be
 * rendered in many markdown editors and visualization tools. The mind map typically 
 * includes a hierarchical structure with multiple levels, showing the relationships
 * between concepts related to the provided topic.
 * 
 * Complex topics like "Computer Science", "Physics", or "Machine Learning" will generate
 * more extensive mind maps with deeper hierarchies compared to simpler topics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for generating a mind map in Mermaid format")
public class MindMapRequest {
    @NotBlank(message = "Topic is required")
    @Size(min = 3, max = 200, message = "Topic must be between 3 and 200 characters")
    @Schema(
        description = "The topic for which to generate a mind map", 
        example = "Artificial Intelligence and Its Applications", 
        required = true
    )
    private String topic;
}
