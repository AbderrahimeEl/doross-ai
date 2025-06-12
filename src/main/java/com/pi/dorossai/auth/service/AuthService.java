package com.pi.dorossai.auth.service;

import com.pi.dorossai.auth.dto.LoginRequest;
import com.pi.dorossai.auth.dto.SignupRequest;
import com.pi.dorossai.security.jwt.JwtUtils;
import com.pi.dorossai.user.model.Role;
import com.pi.dorossai.user.model.User;
import com.pi.dorossai.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;    public Authentication authenticateUser(LoginRequest loginRequest) {
        // Authentication is disabled, so we create a successful authentication regardless of credentials
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                null, 
                java.util.Collections.emptyList());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }    public User registerUser(SignupRequest signupRequest) {
        // Authentication is disabled, so we'll just create the user without much validation
        
        // Check if email exists (optional since auth is disabled)
        boolean emailExists = userRepository.existsByEmail(signupRequest.getEmail());
        
        // If email exists and auth is disabled, we'll still create a new user with a modified email
        String email = signupRequest.getEmail();
        if (emailExists) {
            // Append a random suffix to make the email unique
            email = email + "-" + System.currentTimeMillis();
        }

        // Create new user
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // Set role (default to USER if not specified)
        Role role = signupRequest.getRole();
        if (role == null) {
            role = Role.USER;
        }
        user.setRole(role);

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }
}
