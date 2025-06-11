package com.pi.dorossai.writing.controller;

import com.pi.dorossai.writing.dto.WritingImprovementRequest;
import com.pi.dorossai.writing.dto.WritingImprovementResponse;
import com.pi.dorossai.writing.service.WritingService;
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
@Tag(name = "Writing Improvement", description = "AI-powered writing enhancement services")
public class WritingController {
    
    private final WritingService writingService;
      @PostMapping("/improve-writing")
    @Operation(
        summary = "Improve Writing", 
        description = "Enhance text quality and style using AI. Supports multiple writing styles including professional, academic, casual, and creative. Provides detailed feedback on improvements made."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Writing improved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = WritingImprovementResponse.class),
                examples = @ExampleObject(
                    name = "Improved Writing",
                    value = """
                    {
                        "improvedText": "Artificial intelligence represents a transformative technology that is fundamentally reshaping industries across the globe. Through sophisticated machine learning algorithms and neural networks, AI systems can now perform complex tasks that were previously exclusive to human cognition, including pattern recognition, natural language processing, and predictive analytics.",
                        "changes": [
                            "Enhanced vocabulary with more precise terminology",
                            "Improved sentence structure for better flow",
                            "Added transitional phrases for coherence",
                            "Converted to professional tone",
                            "Corrected grammatical inconsistencies"
                        ],
                        "originalLength": 187,
                        "improvedLength": 321
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
                        "path": "/api/improve-writing",
                        "validationErrors": {
                            "text": "Text cannot be blank",
                            "style": "Style must be one of: professional, academic, creative, casual, formal, technical"
                        },
                        "details": ["text: Text cannot be blank", "style: Style must be one of: professional, academic, creative, casual, formal, technical"],
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
                        "path": "/api/improve-writing",
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
                        "path": "/api/improve-writing",
                        "errorCode": "ERR_AI_SERVICE_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<WritingImprovementResponse> improveWriting(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Writing improvement request with text and desired style",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = WritingImprovementRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Professional Style",
                        value = """
                        {
                            "text": "AI is really cool and its changing everything. Companies are using it for lots of different things and its making a big impact on how we work and live.",
                            "style": "professional"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Academic Style",
                        value = """
                        {
                            "text": "This research looks at how machine learning works and why its important for future technology developments.",
                            "style": "academic"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Creative Style",
                        value = """
                        {
                            "text": "The robot walked across the room. It was very advanced. Technology is amazing.",
                            "style": "creative"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody WritingImprovementRequest request) {
        log.info("Received request to improve writing in {} style for text of length: {}", 
                request.getStyle(), request.getText().length());
        
        WritingImprovementResponse response = writingService.improveWriting(request);
        return ResponseEntity.ok(response);
    }
}
