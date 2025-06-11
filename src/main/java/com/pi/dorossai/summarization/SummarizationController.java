package com.pi.dorossai.summarization;

import com.pi.dorossai.config.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Text Summarization", description = "AI-powered text summarization in multiple languages")
public class SummarizationController {
    
    private final SummarizationService summarizationService;
    
    @PostMapping("/summarize")
    @Operation(
        summary = "Summarize Text", 
        description = "Generate concise summaries of long texts while preserving key information. Supports multiple languages and maintains context."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Text summarized successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SummarizationResponse.class),
                examples = @ExampleObject(
                    name = "Summarized Text",
                    value = """
                    {
                        "summary": "Artificial Intelligence (AI) refers to the simulation of human intelligence in machines programmed to think and learn. AI systems can perform tasks that typically require human intelligence, such as visual perception, speech recognition, and decision-making. Machine learning, a subset of AI, enables systems to automatically learn and improve from experience without being explicitly programmed.",
                        "language": "english",
                        "originalLength": 1250,
                        "summaryLength": 387
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation Error",
                    value = """
                    {
                        "error": "Bad Request",
                        "message": "Text cannot be blank"
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
                examples = @ExampleObject(
                    name = "Unauthorized",
                    value = """
                    {
                        "error": "Unauthorized",
                        "message": "JWT token is required"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> summarizeText(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Text summarization request with content and target language",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SummarizationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "English Article",
                        value = """
                        {
                            "text": "Artificial Intelligence (AI) is a branch of computer science that aims to create intelligent machines that work and react like humans. Some of the activities computers with artificial intelligence are designed for include: speech recognition, learning, planning, and problem solving. AI research has been highly successful in developing effective techniques for solving a wide range of problems, from game playing to medical diagnosis. The goals of AI research include reasoning, knowledge representation, planning, learning, natural language processing, perception and the ability to move and manipulate objects. General intelligence is among the field's long-term goals. Approaches include statistical methods, computational intelligence, and traditional symbolic AI.",
                            "language": "english"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Technical Document",
                        value = """
                        {
                            "text": "Machine learning is a method of data analysis that automates analytical model building. It is a branch of artificial intelligence based on the idea that systems can learn from data, identify patterns and make decisions with minimal human intervention. Machine learning algorithms build a mathematical model based on training data, in order to make predictions or decisions without being explicitly programmed to do so. Machine learning algorithms are used in a wide variety of applications, such as email filtering and computer vision, where it is difficult or infeasible to develop conventional algorithms to perform the needed tasks.",
                            "language": "english"
                        }
                        """
                    )
                }
            )
        )        @Valid @RequestBody SummarizationRequest request) {
        log.info("Received request to summarize text of length: {}", request.getText().length());
        
        try {
            SummarizationResponse response = summarizationService.summarizeText(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error during text summarization", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message(e.getMessage())
                .path("/api/summarize")
                .errorCode("ERR_VALIDATION_001")
                .errorCategory("VALIDATION_ERROR")
                .build();
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error during text summarization", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("AI summarization service is temporarily unavailable")
                .path("/api/summarize")
                .errorCode("ERR_AI_SERVICE_001")
                .errorCategory("AI_SERVICE_ERROR")
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}