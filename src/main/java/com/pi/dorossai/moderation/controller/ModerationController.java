package com.pi.dorossai.moderation.controller;

import com.pi.dorossai.config.ErrorResponse;
import com.pi.dorossai.moderation.dto.ModerationRequest;
import com.pi.dorossai.moderation.dto.ModerationResponse;
import com.pi.dorossai.moderation.service.ModerationService;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Content Moderation", description = "AI-powered content moderation and safety analysis")
public class ModerationController {
    
    private final ModerationService moderationService;
    
    @PostMapping("/analyze")
    @Operation(
        summary = "Analyze content for safety and moderation",
        description = "Uses AI to analyze text content for potential policy violations, harmful content, or safety concerns",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Content analysis completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ModerationResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Safe Content",
                        summary = "Analysis of safe content",
                        value = """
                        {
                            "content": "This is a friendly greeting message",
                            "isSafe": true,
                            "safetyLevel": "LOW_RISK",
                            "categories": {
                                "hate": 0.01,
                                "harassment": 0.02,
                                "violence": 0.00,
                                "sexual": 0.01,
                                "self_harm": 0.00
                            },
                            "confidence": 0.98,
                            "reasoning": "Content appears to be a normal, friendly message with no harmful intent detected."
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Moderate Risk Content",
                        summary = "Analysis of content with moderate risk",
                        value = """
                        {
                            "content": "This person is really annoying me",
                            "isSafe": true,
                            "safetyLevel": "MEDIUM_RISK",
                            "categories": {
                                "hate": 0.15,
                                "harassment": 0.25,
                                "violence": 0.05,
                                "sexual": 0.01,
                                "self_harm": 0.02
                            },
                            "confidence": 0.85,
                            "reasoning": "Content expresses mild frustration but does not cross into harmful territory."
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - missing or invalid content",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Missing Content",
                        summary = "Content field is required",
                        value = """
                        {
                            "timestamp": "2025-06-11T10:30:00Z",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Content is required for moderation analysis",
                            "path": "/api/moderation/analyze",
                            "errorCode": "ERR_VALIDATION_001",
                            "errorCategory": "VALIDATION_ERROR",
                            "validationErrors": [
                                {
                                    "field": "content",
                                    "rejectedValue": null,
                                    "message": "Content cannot be null or empty"
                                }
                            ]
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Unauthorized",
                    summary = "Authentication token missing or invalid",
                    value = """
                    {
                        "timestamp": "2025-06-11T10:30:00Z",
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Authentication required to access this resource",
                        "path": "/api/moderation/analyze",
                        "errorCode": "ERR_AUTH_001",
                        "errorCategory": "AUTHENTICATION_ERROR"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error - AI service unavailable",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "AI Service Error",
                    summary = "Moderation AI service is temporarily unavailable",
                    value = """
                    {
                        "timestamp": "2025-06-11T10:30:00Z",
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "AI moderation service is temporarily unavailable",
                        "path": "/api/moderation/analyze",
                        "errorCode": "ERR_AI_SERVICE_001",
                        "errorCategory": "AI_SERVICE_ERROR"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> moderateContent(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Content moderation request with text and moderation level",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ModerationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Standard Moderation",
                        value = """
                        {
                            "text": "This is a sample text for moderation testing. It contains normal conversation content.",
                            "level": "standard"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Strict Moderation",
                        value = """
                        {
                            "text": "User comment or post content that needs strict content review.",
                            "level": "strict"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Lenient Moderation",
                        value = """
                        {
                            "text": "Casual discussion content where only serious violations should be flagged.",
                            "level": "lenient"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody ModerationRequest request) {
        log.info("Received content moderation request at {} level for text of length: {}", 
                request.getLevel(), request.getText().length());
        
        try {
            ModerationResponse response = moderationService.moderateContent(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error during moderation analysis", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message(e.getMessage())
                .path("/api/moderation/analyze")
                .errorCode("ERR_VALIDATION_001")
                .errorCategory("VALIDATION_ERROR")
                .build();
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error during moderation analysis", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("AI moderation service is temporarily unavailable")
                .path("/api/moderation/analyze")
                .errorCode("ERR_AI_SERVICE_001")
                .errorCategory("AI_SERVICE_ERROR")
                .build();
            return ResponseEntity.status(500).body(error);
        }
    }
}
