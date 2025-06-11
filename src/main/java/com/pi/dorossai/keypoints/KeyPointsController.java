package com.pi.dorossai.keypoints;

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
@Tag(name = "Key Points Extraction", description = "AI-powered extraction of key points from text content")
public class KeyPointsController {
    
    private final KeyPointsService keyPointsService;
    
    @PostMapping("/extract-key-points")
    @Operation(
        summary = "Extract Key Points", 
        description = "Extract the most important and meaningful points from text content. Perfect for creating study notes, meeting summaries, or content highlights."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Key points extracted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KeyPointsResponse.class),
                examples = @ExampleObject(
                    name = "Extracted Key Points",
                    value = """
                    {
                        "keyPoints": [
                            "Artificial Intelligence simulates human intelligence in machines designed to think and learn like humans.",
                            "Machine learning is a subset of AI that enables systems to learn and improve from experience automatically.",
                            "AI applications include speech recognition, computer vision, natural language processing, and decision-making systems.",
                            "Deep learning uses neural networks with multiple layers to process complex data patterns.",
                            "AI research focuses on creating systems that can reason, learn, perceive, and manipulate objects autonomously."
                        ],
                        "originalLength": 1847
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
    public ResponseEntity<?> extractKeyPoints(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Key points extraction request with text content and number of points",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KeyPointsRequest.class),                examples = {
                    @ExampleObject(
                        name = "Research Article",
                        value = """
                        {
                            "text": "Artificial Intelligence (AI) is transforming industries worldwide by providing machines with the ability to simulate human intelligence. Machine learning, a core subset of AI, allows systems to automatically learn and improve from experience without explicit programming. Deep learning, which uses neural networks with multiple hidden layers, has revolutionized fields like computer vision and natural language processing. AI applications now span across healthcare for medical diagnosis, finance for fraud detection, autonomous vehicles for transportation, and virtual assistants for daily tasks. The technology continues to evolve with advances in quantum computing and edge AI, promising even more sophisticated applications in the future.",
                            "numPoints": 5
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Meeting Notes",
                        value = """
                        {
                            "text": "Today's team meeting covered several important topics. First, we discussed the upcoming product launch scheduled for next month and identified key marketing strategies. The development team reported that all major features are complete and testing is in progress. We also reviewed budget allocations for Q4 and decided to increase investment in customer support. Additionally, HR announced new hiring plans for the engineering team, targeting 5 new developers by year-end. Finally, we scheduled weekly check-ins to monitor progress and address any blockers.",
                            "numPoints": 3
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Default Points",
                        value = """
                        {
                            "text": "Climate change is one of the most pressing issues of our time, with rising global temperatures, melting ice caps, and extreme weather events becoming more frequent. The main causes include greenhouse gas emissions from fossil fuels, deforestation, and industrial processes."
                        }
                        """
                    )
                }
            )
        )        @Valid @RequestBody KeyPointsRequest request) {
        log.info("Received request to extract key points from text of length: {}", request.getText().length());
        
        try {
            KeyPointsResponse response = keyPointsService.extractKeyPoints(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error during key points extraction", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message(e.getMessage())
                .path("/api/extract-key-points")
                .errorCode("ERR_VALIDATION_001")
                .errorCategory("VALIDATION_ERROR")
                .build();
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            log.error("Error during key points extraction", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("AI key points extraction service is temporarily unavailable")
                .path("/api/extract-key-points")
                .errorCode("ERR_AI_SERVICE_001")
                .errorCategory("AI_SERVICE_ERROR")
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}