package com.pi.dorossai.quiz;

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
@Tag(name = "Quiz Generation", description = "AI-powered quiz generation with multiple choice questions")
public class QuizController {
    
    private final QuizService quizService;
      @PostMapping("/generate-quiz")
    @Operation(
        summary = "Generate Quiz", 
        description = "Generate a multiple-choice quiz on any topic with customizable difficulty and language. Perfect for assessments and learning evaluation."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Quiz generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuizResponse.class),
                examples = @ExampleObject(
                    name = "Generated Quiz",
                    value = """
                    {
                        "quiz": [
                            {
                                "question": "Which programming language is known for its use in data science and machine learning?",
                                "options": ["Java", "Python", "C++", "JavaScript"],
                                "correctAnswer": "Python",
                                "explanation": "Python is widely used in data science and machine learning due to its extensive libraries like NumPy, Pandas, and TensorFlow."
                            },
                            {
                                "question": "What does 'AI' stand for?",
                                "options": ["Automated Intelligence", "Artificial Intelligence", "Advanced Integration", "Algorithmic Interface"],
                                "correctAnswer": "Artificial Intelligence",
                                "explanation": "AI stands for Artificial Intelligence, which refers to the simulation of human intelligence in machines."
                            }
                        ],
                        "topic": "artificial intelligence",
                        "difficulty": "medium",
                        "language": "english"
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
                        "path": "/api/generate-quiz",
                        "validationErrors": {
                            "topic": "Topic cannot be blank",
                            "numQuestions": "Number of questions must be between 1 and 20"
                        },
                        "details": ["topic: Topic cannot be blank", "numQuestions: Number of questions must be between 1 and 20"],
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
                        "path": "/api/generate-quiz",
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
                        "path": "/api/generate-quiz",
                        "errorCode": "ERR_AI_SERVICE_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<QuizResponse> generateQuiz(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Quiz generation request with topic, difficulty, and other options",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = QuizGenerationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Basic Quiz",
                        value = """
                        {
                            "topic": "artificial intelligence",
                            "numQuestions": 5,
                            "difficulty": "medium",
                            "language": "english"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Advanced Quiz",
                        value = """
                        {
                            "topic": "quantum physics",
                            "numQuestions": 10,
                            "difficulty": "hard",
                            "language": "english"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Beginner Quiz",
                        value = """
                        {
                            "topic": "basic mathematics",
                            "numQuestions": 3,
                            "difficulty": "easy",
                            "language": "english"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody QuizGenerationRequest request) {
        log.info("Received request to generate quiz on topic: {}", request.getTopic());
        QuizResponse response = quizService.generateQuiz(request);
        return ResponseEntity.ok(response);
    }
}