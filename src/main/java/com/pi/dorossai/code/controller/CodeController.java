package com.pi.dorossai.code.controller;

import com.pi.dorossai.code.dto.CodeExplanationRequest;
import com.pi.dorossai.code.dto.CodeExplanationResponse;
import com.pi.dorossai.code.service.CodeService;
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
@Tag(name = "Code Analysis", description = "Code explanation and analysis services")
public class CodeController {
    
    private final CodeService codeService;
      @PostMapping("/explain-code")
    @Operation(
        summary = "Explain Code", 
        description = "Analyze and explain code functionality at different detail levels. Supports multiple programming languages and provides detailed explanations of code logic, structure, and best practices."
    )
    @SecurityRequirement(name = "BearerAuth")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Code explained successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CodeExplanationResponse.class),
                examples = @ExampleObject(
                    name = "Code Explanation",
                    value = """
                    {
                        "explanation": "This Python function implements the Fibonacci sequence using recursion. The function takes an integer 'n' as input and returns the nth Fibonacci number. The base cases handle when n is 0 or 1, returning n directly. For larger values, it recursively calls itself with (n-1) and (n-2) and adds the results. While this implementation is straightforward, it has exponential time complexity O(2^n) due to repeated calculations. For better performance, consider using dynamic programming or iterative approaches.",
                        "language": "python",
                        "detailLevel": "intermediate"
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
                        "path": "/api/explain-code",
                        "validationErrors": {
                            "code": "Code cannot be blank",
                            "language": "Language must be specified"
                        },
                        "details": ["code: Code cannot be blank", "language: Language must be specified"],
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
                        "path": "/api/explain-code",
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
                        "path": "/api/explain-code",
                        "errorCode": "ERR_AI_SERVICE_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<CodeExplanationResponse> explainCode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Code explanation request with source code and analysis parameters",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CodeExplanationRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Python Function",
                        value = """
                        {
                            "code": "def fibonacci(n):\\n    if n <= 1:\\n        return n\\n    return fibonacci(n-1) + fibonacci(n-2)",
                            "language": "python",
                            "detailLevel": "intermediate"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "JavaScript Algorithm",
                        value = """
                        {
                            "code": "function binarySearch(arr, target) {\\n    let left = 0;\\n    let right = arr.length - 1;\\n    while (left <= right) {\\n        let mid = Math.floor((left + right) / 2);\\n        if (arr[mid] === target) return mid;\\n        if (arr[mid] < target) left = mid + 1;\\n        else right = mid - 1;\\n    }\\n    return -1;\\n}",
                            "language": "javascript",
                            "detailLevel": "advanced"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Java Class",
                        value = """
                        {
                            "code": "public class Calculator {\\n    public int add(int a, int b) {\\n        return a + b;\\n    }\\n    public int multiply(int a, int b) {\\n        return a * b;\\n    }\\n}",
                            "language": "java",
                            "detailLevel": "beginner"
                        }
                        """
                    )
                }
            )
        )
        @Valid @RequestBody CodeExplanationRequest request) {
        log.info("Received request to explain {} code at {} level", request.getLanguage(), request.getDetailLevel());
        
        CodeExplanationResponse response = codeService.explainCode(request);
        return ResponseEntity.ok(response);
    }
}
