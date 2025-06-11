package com.pi.dorossai.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

//@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        List<String> details = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
            details.add(String.format("%s: %s", fieldName, errorMessage));
        });

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Request validation failed. Please check the provided data.",
            request.getRequestURI(),
            validationErrors
        );
        errorResponse.setDetails(details);
        errorResponse.setErrorCode("ERR_VALIDATION_001");
        
        log.warn("Validation error on path {}: {}", request.getRequestURI(), validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Constraint violation errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationExceptions(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        List<String> details = new ArrayList<>();
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            validationErrors.put(fieldName, errorMessage);
            details.add(String.format("%s: %s", fieldName, errorMessage));
        }

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Constraint Violation",
            "Data constraints were violated. Please check the provided data.",
            request.getRequestURI(),
            validationErrors
        );
        errorResponse.setDetails(details);
        errorResponse.setErrorCode("ERR_CONSTRAINT_001");
        
        log.warn("Constraint violation error on path {}: {}", request.getRequestURI(), validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Authentication errors
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication Failed",
            "Invalid credentials or authentication token. Please check your login details.",
            request.getRequestURI(),
            "ERR_AUTH_001"
        );
        
        log.warn("Authentication error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Authorization errors
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access Denied",
            "You don't have permission to access this resource. Admin role may be required.",
            request.getRequestURI(),
            "ERR_AUTH_002"
        );
        
        log.warn("Access denied error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Entity not found errors
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI(),
            "ERR_NOTFOUND_001"
        );
        
        log.warn("Entity not found error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Request",
            ex.getMessage(),
            request.getRequestURI(),
            "ERR_INVALID_001"
        );
        
        log.warn("Illegal argument error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // HTTP method not supported
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        String supportedMethods = String.join(", ", ex.getSupportedMethods());
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            "Method Not Allowed",
            String.format("HTTP method '%s' is not supported for this endpoint. Supported methods: %s", 
                         ex.getMethod(), supportedMethods),
            request.getRequestURI(),
            "ERR_METHOD_001"
        );
        
        log.warn("Method not allowed error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Missing request parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Missing Parameter",
            String.format("Required parameter '%s' of type '%s' is missing", 
                         ex.getParameterName(), ex.getParameterType()),
            request.getRequestURI(),
            "ERR_PARAM_001"
        );
        
        log.warn("Missing parameter error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Type mismatch errors
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Type Mismatch",
            String.format("Parameter '%s' should be of type '%s' but received '%s'", 
                         ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue()),
            request.getRequestURI(),
            "ERR_TYPE_001"
        );
        
        log.warn("Type mismatch error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // JSON parsing errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParsingException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        String message = "Invalid JSON format or missing request body";
        if (ex.getMessage().contains("Required request body is missing")) {
            message = "Request body is required but was not provided";
        } else if (ex.getMessage().contains("JSON parse error")) {
            message = "Invalid JSON format. Please check your request body syntax";
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Request Body",
            message,
            request.getRequestURI(),
            "ERR_JSON_001"
        );
        
        log.warn("JSON parsing error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // No handler found (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Endpoint Not Found",
            String.format("No endpoint found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
            request.getRequestURI(),
            "ERR_ENDPOINT_001"
        );
        
        log.warn("No handler found error: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Timeout errors
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(
            TimeoutException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.REQUEST_TIMEOUT.value(),
            "Request Timeout",
            "The request took too long to process. Please try again later or contact support if the problem persists.",
            request.getRequestURI(),
            "ERR_TIMEOUT_001"
        );
        
        log.error("Timeout error on path {}: {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.REQUEST_TIMEOUT);
    }

    // AI Service specific errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        
        String message = ex.getMessage();
        String errorCode = "ERR_RUNTIME_001";
        
        // Check for specific AI service errors
        if (message != null) {
            if (message.contains("AI service") || message.contains("timeout")) {
                errorCode = "ERR_AI_SERVICE_001";
                message = "AI service is temporarily unavailable. Please try again later.";
            } else if (message.contains("Failed to generate") || message.contains("Failed to")) {
                errorCode = "ERR_AI_GENERATION_001";
                message = "Failed to generate content. Please check your input and try again.";
            } else if (message.contains("Email is already")) {
                errorCode = "ERR_DUPLICATE_001";
                // Keep original message for email conflicts
            }
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            message != null ? message : "An unexpected error occurred. Please try again later.",
            request.getRequestURI(),
            errorCode
        );
        
        log.error("Runtime error on path {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please try again later or contact support if the problem persists.",
            request.getRequestURI(),
            "ERR_GENERIC_001"
        );
        
        log.error("Unexpected error on path {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
