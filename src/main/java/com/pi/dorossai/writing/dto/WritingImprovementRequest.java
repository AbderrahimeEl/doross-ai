package com.pi.dorossai.writing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for writing improvement with style preferences")
public class WritingImprovementRequest {
    @NotBlank
    @Schema(
        description = "The text to be improved",
        example = "AI is really cool and its changing everything. Companies are using it for lots of different things and its making a big impact on how we work and live.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String text;
    
    @Schema(
        description = "The writing style to apply",
        example = "professional",
        allowableValues = {"professional", "academic", "creative", "casual", "formal", "technical"},
        defaultValue = "professional"
    )
    private String style = "professional";
}
