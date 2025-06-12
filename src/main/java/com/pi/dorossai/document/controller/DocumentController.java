package com.pi.dorossai.document.controller;

import com.pi.dorossai.document.dto.DocumentQARequest;
import com.pi.dorossai.document.dto.DocumentQAResponse;
import com.pi.dorossai.document.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Document Q&A", description = "Document-based question answering services")
public class DocumentController {
    
    private final DocumentService documentService;    @PostMapping("/ask-document")
    @Operation(
        summary = "Ask Document", 
        description = "Answer questions based on provided document context. Uses AI to analyze the document content and provide accurate, contextual answers with confidence levels."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Question answered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DocumentQAResponse.class),
                examples = @ExampleObject(
                    name = "Document Answer",
                    value = """
                    {
                        "answer": "Spring Boot is a Java-based framework used to create microservices. It provides a platform for Java developers to develop stand-alone and production-grade spring applications with minimal configuration requirements.",
                        "confidence": "high"
                    }
                    """
                )
            )
        ),        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Validation Error",
                    value = """
                    {
                        "status": 400,
                        "error": "Validation Failed",
                        "message": "Request validation failed. Please check the provided data.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/ask-document",
                        "validationErrors": {
                            "question": "Question cannot be blank",
                            "context": "Context cannot be blank"
                        },
                        "details": ["question: Question cannot be blank", "context: Context cannot be blank"],
                        "errorCode": "ERR_VALIDATION_001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - JWT token required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Unauthorized",
                    value = """
                    {
                        "status": 401,
                        "error": "Authentication Failed",
                        "message": "Invalid credentials or authentication token. Please check your login details.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/ask-document",
                        "errorCode": "ERR_AUTH_001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "AI service error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "AI Service Error",
                    value = """
                    {
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "AI service is temporarily unavailable. Please try again later.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/ask-document",
                        "errorCode": "ERR_AI_SERVICE_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<DocumentQAResponse> askDocument(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Document Q&A request with question and context",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DocumentQARequest.class),
                examples = {
                    @ExampleObject(
                        name = "Technical Documentation",
                        value = """
                        {
                            "question": "What is Spring Boot and what are its main advantages?",
                            "context": "Spring Boot is a Java-based framework used to create microservices. It provides a good platform for Java developers to develop stand-alone and production-grade spring applications that you can just run. You can get started with minimum configurations without the need for an entire Spring configuration setup. Spring Boot offers automatic configuration, embedded servers, and production-ready features like health checks and metrics."
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Research Paper",
                        value = """
                        {
                            "question": "What are the main findings of this study?",
                            "context": "This research study analyzed the impact of artificial intelligence on workplace productivity. The study surveyed 500 companies across various industries over a 12-month period. Results showed that companies implementing AI solutions experienced an average productivity increase of 23%, with the highest gains in data processing and customer service departments. However, 34% of companies reported initial challenges with employee adaptation and training requirements."
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Policy Document",
                        value = """
                        {
                            "question": "What are the eligibility requirements?",
                            "context": "To be eligible for this program, applicants must meet the following criteria: 1) Be at least 18 years of age, 2) Have completed high school or equivalent education, 3) Demonstrate financial need through income documentation, 4) Be a legal resident of the state for at least 12 months, 5) Not have participated in a similar program within the past 3 years."
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody DocumentQARequest request) {
        log.info("Received document Q&A request for question: {}", request.getQuestion());
        
        DocumentQAResponse response = documentService.askDocument(request);
        return ResponseEntity.ok(response);
    }
}
