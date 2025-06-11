package com.pi.dorossai.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing the answer to a document-based question")
public class DocumentQAResponse {
    @Schema(
        description = "The answer extracted from the document context",
        example = "Spring Boot is a Java-based framework used to create microservices. It provides a platform for Java developers to develop stand-alone and production-grade spring applications with minimal configuration requirements."
    )
    private String answer;
    
    @Schema(
        description = "Confidence level of the answer accuracy",
        example = "high",
        allowableValues = {"low", "medium", "high"}
    )
    private String confidence;
}
