package com.pi.dorossai.auth.controller;

import com.pi.dorossai.auth.dto.JwtResponse;
import com.pi.dorossai.auth.dto.LoginRequest;
import com.pi.dorossai.auth.dto.SignupRequest;
import com.pi.dorossai.auth.service.AuthService;
import com.pi.dorossai.security.jwt.JwtUtils;
import com.pi.dorossai.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(
        summary = "User Login", 
        description = "Authenticate a user with email and password. Returns JWT token for accessing protected endpoints."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class),
                examples = @ExampleObject(
                    name = "Successful Login",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "type": "Bearer",
                        "id": 1,
                        "name": "John Doe",
                        "email": "john.doe@example.com",
                        "role": "USER"
                    }
                    """
                )
            )
        ),        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Invalid Login",
                    value = """
                    {
                        "status": 401,
                        "error": "Authentication Failed",
                        "message": "Invalid credentials or authentication token. Please check your login details.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/auth/login",
                        "errorCode": "ERR_AUTH_001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Validation error",
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
                        "path": "/api/auth/login",
                        "validationErrors": {
                            "email": "Email cannot be blank",
                            "password": "Password cannot be blank"
                        },
                        "details": ["email: Email cannot be blank", "password: Password cannot be blank"],
                        "errorCode": "ERR_VALIDATION_001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Server Error",
                    value = """
                    {
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "An unexpected error occurred. Please try again later or contact support if the problem persists.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/auth/login",
                        "errorCode": "ERR_GENERIC_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<JwtResponse> authenticateUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Login Request",
                    value = """
                    {
                        "email": "john.doe@example.com",
                        "password": "securePassword123"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.authenticateUser(loginRequest);

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = authService.getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()));
    }

    @PostMapping("/signup")
    @Operation(
        summary = "User Registration", 
        description = "Register a new user account. Returns JWT token for immediate access after registration."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Registration successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class),
                examples = @ExampleObject(
                    name = "Successful Registration",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "type": "Bearer",
                        "id": 2,
                        "name": "Jane Smith",
                        "email": "jane.smith@example.com",
                        "role": "USER"
                    }
                    """
                )
            )
        ),        @ApiResponse(
            responseCode = "400", 
            description = "Email already exists or validation error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Email Already Exists",
                        value = """
                        {
                            "status": 400,
                            "error": "Invalid Request",
                            "message": "Email is already in use",
                            "timestamp": "2025-06-11T10:30:00",
                            "path": "/api/auth/signup",
                            "errorCode": "ERR_DUPLICATE_001"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Validation Error",
                        value = """
                        {
                            "status": 400,
                            "error": "Validation Failed",
                            "message": "Request validation failed. Please check the provided data.",
                            "timestamp": "2025-06-11T10:30:00",
                            "path": "/api/auth/signup",
                            "validationErrors": {
                                "name": "Name must be between 3 and 50 characters",
                                "email": "Email format is invalid",
                                "password": "Password must be between 6 and 40 characters"
                            },
                            "details": ["name: Name must be between 3 and 50 characters", "email: Email format is invalid", "password: Password must be between 6 and 40 characters"],
                            "errorCode": "ERR_VALIDATION_001"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.pi.dorossai.config.ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Server Error",
                    value = """
                    {
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "An unexpected error occurred during registration. Please try again later.",
                        "timestamp": "2025-06-11T10:30:00",
                        "path": "/api/auth/signup",
                        "errorCode": "ERR_GENERIC_001"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<JwtResponse> registerUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration information",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignupRequest.class),
                examples = @ExampleObject(
                    name = "Registration Request",
                    value = """
                    {
                        "name": "Jane Smith",
                        "email": "jane.smith@example.com",
                        "password": "securePassword123",
                        "role": "USER"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody SignupRequest signupRequest) {
        User user = authService.registerUser(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(signupRequest.getPassword());

        Authentication authentication = authService.authenticateUser(loginRequest);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()));
    }
}
