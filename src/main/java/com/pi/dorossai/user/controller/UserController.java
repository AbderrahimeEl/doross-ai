package com.pi.dorossai.user.controller;

import com.pi.dorossai.config.ErrorResponse;
import com.pi.dorossai.user.model.User;
import com.pi.dorossai.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "User management operations (Authentication disabled)")
public class UserController {
    private final UserService userService;    @GetMapping    @Operation(
        summary = "Get All Users", 
        description = "Retrieve a list of all users in the system. Authentication is disabled."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array", implementation = User.class),
                examples = @ExampleObject(
                    name = "Users List",
                    value = """
                    [
                        {
                            "id": 1,
                            "name": "John Doe",
                            "email": "john.doe@example.com",
                            "role": "USER"
                        },
                        {
                            "id": 2,
                            "name": "Jane Admin",
                            "email": "jane.admin@example.com",
                            "role": "ADMIN"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied - Admin role required",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Access Denied",
                    value = """
                    {
                        "error": "Forbidden",
                        "message": "Access denied - Admin role required"
                    }
                    """
                )
            )
        )
    })    public ResponseEntity<?> getAllUsers() {
        try {
            log.info("Fetching all users");
            List<User> users = userService.getAllUsers();
            log.info("Successfully retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error retrieving all users", e);
            ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("Unable to retrieve users")
                .path("/api/users")
                .errorCode("ERR_USER_SERVICE_001")
                .errorCategory("USER_SERVICE_ERROR")
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }    @GetMapping("/{id}")    @Operation(
        summary = "Get User by ID", 
        description = "Retrieve a specific user by their ID. Authentication is disabled."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "User Details",
                    value = """
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john.doe@example.com",
                        "role": "USER"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Not Found",
                    value = """
                    {
                        "error": "Not Found",
                        "message": "User not found with id: 999"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Access Denied",
                    value = """
                    {
                        "error": "Forbidden",
                        "message": "Access denied"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<User> getUserById(
        @Parameter(description = "User ID", required = true, example = "1")
        @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }    @PostMapping    @Operation(
        summary = "Create User", 
        description = "Create a new user account. Authentication is disabled."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "Created User",
                    value = """
                    {
                        "id": 3,
                        "name": "New User",
                        "email": "new.user@example.com",
                        "role": "USER"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user data or email already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation Error",
                    value = """
                    {
                        "error": "Bad Request",
                        "message": "Email is already taken!"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<User> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User creation data",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "New User",
                    value = """
                    {
                        "name": "New User",
                        "email": "new.user@example.com",
                        "password": "securePassword123",
                        "role": "USER"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }    @PutMapping("/{id}")    @Operation(
        summary = "Update User", 
        description = "Update an existing user. Authentication is disabled."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "Updated User",
                    value = """
                    {
                        "id": 1,
                        "name": "John Doe Updated",
                        "email": "john.doe.updated@example.com",
                        "role": "USER"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Not Found",
                    value = """
                    {
                        "error": "Not Found",
                        "message": "User not found with id: 999"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<User> updateUser(
        @Parameter(description = "User ID", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated user data",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "Update User",
                    value = """
                    {
                        "name": "John Doe Updated",
                        "email": "john.doe.updated@example.com",
                        "password": "newPassword123",
                        "role": "USER"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }    @DeleteMapping("/{id}")    @Operation(
        summary = "Delete User", 
        description = "Delete a user account. Authentication is disabled."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Not Found",
                    value = """
                    {
                        "error": "Not Found",
                        "message": "User not found with id: 999"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied - Admin role required",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Access Denied",
                    value = """
                    {
                        "error": "Forbidden",
                        "message": "Access denied - Admin role required"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "User ID", required = true, example = "1")
        @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}