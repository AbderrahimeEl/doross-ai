package com.pi.dorossai.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request for document-based question answering")
public class DocumentQARequest {
    @NotBlank
    @Schema(
        description = "The question to ask about the document",
        example = "What is Spring Boot and what are its main advantages?",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String question;
    
    @NotBlank
    @Schema(
        description = "The document content or context to analyze",
        example = "Spring Boot is a Java-based framework used to create microservices. It provides a good platform for Java developers to develop stand-alone and production-grade spring applications that you can just run. You can get started with minimum configurations without the need for an entire Spring configuration setup. Spring Boot offers automatic configuration, embedded servers, and production-ready features like health checks and metrics.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String context;
}
