package com.pi.dorossai.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard error response format with detailed error information")
public class ErrorResponse {
    
    @Schema(
        description = "HTTP status code", 
        example = "400"
    )
    private int status;
    
    @Schema(
        description = "Error type/category", 
        example = "Bad Request"
    )
    private String error;
    
    @Schema(
        description = "Detailed error message", 
        example = "Validation failed for request parameters"
    )
    private String message;
    
    @Schema(
        description = "Timestamp when the error occurred", 
        example = "2025-06-11T10:30:00"
    )
    private LocalDateTime timestamp;
    
    @Schema(
        description = "Request path where error occurred", 
        example = "/api/generate-flashcards"
    )
    private String path;
    
    @Schema(
        description = "Validation errors for specific fields",
        example = "{\"topic\": \"Topic cannot be blank\", \"numCards\": \"Number of cards must be between 1 and 20\"}"
    )
    private Map<String, String> validationErrors;
    
    @Schema(
        description = "List of error details for multiple issues",
        example = "[\"Email format is invalid\", \"Password must be at least 6 characters\"]"
    )
    private List<String> details;
      @Schema(
        description = "Unique error code for tracking",
        example = "ERR_VALIDATION_001"
    )
    private String errorCode;
    
    @Schema(
        description = "Category of the error for classification",
        example = "VALIDATION_ERROR"
    )
    private String errorCategory;

    // Constructor for simple errors
    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for validation errors
    public ErrorResponse(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with error code
    public ErrorResponse(int status, String error, String message, String path, String errorCode) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}
