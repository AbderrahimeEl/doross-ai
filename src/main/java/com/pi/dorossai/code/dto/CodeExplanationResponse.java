package com.pi.dorossai.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing detailed code explanation")
public class CodeExplanationResponse {
    @Schema(
        description = "Detailed explanation of the code functionality and implementation",
        example = "This is a recursive implementation of the Fibonacci sequence. The function takes an integer 'n' as input and returns the nth Fibonacci number. It uses a base case where if n is 0 or 1, it returns n directly. For larger values, it recursively calls itself with n-1 and n-2, then adds the results together. This implementation demonstrates the mathematical definition of Fibonacci numbers but has exponential time complexity due to repeated calculations."
    )
    private String explanation;
    
    @Schema(
        description = "Programming language of the analyzed code",
        example = "python"
    )
    private String language;
    
    @Schema(
        description = "Detail level used for the explanation",
        example = "intermediate"
    )
    private String detailLevel;
}
