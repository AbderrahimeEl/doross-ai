package com.pi.dorossai.moderation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for content moderation with customizable safety levels")
public class ModerationRequest {
    @NotBlank
    @Schema(
        description = "The text content to be moderated for safety and appropriateness",
        example = "This is a sample text for moderation testing. It contains normal conversation content.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String text;
    
    @Schema(
        description = "Moderation strictness level",
        example = "standard",
        allowableValues = {"strict", "standard", "lenient"},
        defaultValue = "standard"
    )
    private String level = "standard"; // strict, standard, lenient
}
