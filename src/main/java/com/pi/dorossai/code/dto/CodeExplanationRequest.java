package com.pi.dorossai.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for code explanation with customizable detail level")
public class CodeExplanationRequest {
    @NotBlank
    @Schema(
        description = "The source code to be explained",
        example = "def fibonacci(n):\n    if n <= 1:\n        return n\n    return fibonacci(n-1) + fibonacci(n-2)",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String code;
    
    @Schema(
        description = "Programming language of the code",
        example = "python",
        allowableValues = {"python", "java", "javascript", "typescript", "c++", "c#", "go", "rust", "php", "ruby", "swift", "kotlin", "scala"},
        defaultValue = "python"
    )
    private String language = "python";
    
    @Schema(
        description = "Level of detail for the explanation",
        example = "intermediate",
        allowableValues = {"beginner", "intermediate", "advanced"},
        defaultValue = "intermediate"
    )
    private String detailLevel = "intermediate";
}
