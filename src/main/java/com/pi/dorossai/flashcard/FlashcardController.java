package com.pi.dorossai.flashcard;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flashcards", description = "AI-powered flashcard generation for educational content")
public class FlashcardController {
    
    private final FlashcardService flashcardService;
    
    @PostMapping("/generate-flashcards")
    @Operation(
        summary = "Generate Flashcards", 
        description = "Generate educational flashcards on any topic using AI. Perfect for studying and memorization."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Flashcards generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlashcardResponse.class),
                examples = @ExampleObject(
                    name = "Generated Flashcards",
                    value = """
                    {
                        "flashcards": [
                            {
                                "question": "What is photosynthesis?",
                                "answer": "Photosynthesis is the process by which plants use sunlight, water, and carbon dioxide to create oxygen and energy in the form of sugar."
                            },
                            {
                                "question": "What are the main components needed for photosynthesis?",
                                "answer": "The main components are sunlight, water (H2O), carbon dioxide (CO2), and chlorophyll in plant cells."
                            },
                            {
                                "question": "What is the chemical equation for photosynthesis?",
                                "answer": "6CO2 + 6H2O + light energy → C6H12O6 + 6O2"
                            }
                        ],
                        "topic": "photosynthesis",
                        "numCards": 3
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
                        "path": "/api/generate-flashcards",
                        "validationErrors": {
                            "topic": "Topic cannot be blank",
                            "numCards": "Number of cards must be between 1 and 20"
                        },
                        "details": ["topic: Topic cannot be blank", "numCards: Number of cards must be between 1 and 20"],
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
                        "path": "/api/generate-flashcards",
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
                examples = {
                    @ExampleObject(
                        name = "AI Service Error",
                        value = """
                        {
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "AI service is temporarily unavailable. Please try again later.",
                            "timestamp": "2025-06-11T10:30:00",
                            "path": "/api/generate-flashcards",
                            "errorCode": "ERR_AI_SERVICE_001"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Generation Failed",
                        value = """
                        {
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "Failed to generate content. Please check your input and try again.",
                            "timestamp": "2025-06-11T10:30:00",
                            "path": "/api/generate-flashcards",
                            "errorCode": "ERR_AI_GENERATION_001"                        }
                        """
                    )
                }
            )
        )
    })
    public ResponseEntity<FlashcardResponse> generateFlashcards(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Flashcard generation request with topic and number of cards",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FlashcardRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Basic Request",
                        value = """
                        {
                            "topic": "photosynthesis",
                            "numCards": 5
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Maximum Cards",
                        value = """
                        {
                            "topic": "machine learning fundamentals",
                            "numCards": 20
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Default Number",
                        value = """
                        {
                            "topic": "World War II"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody FlashcardRequest request) {
        log.info("Received request to generate flashcards on topic: {}", request.getTopic());
        FlashcardResponse response = flashcardService.generateFlashcards(request);
        return ResponseEntity.ok(response);
    }
}