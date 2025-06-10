package com.pi.dorossai.code.service;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.code.dto.CodeExplanationRequest;
import com.pi.dorossai.code.dto.CodeExplanationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {
    
    private final AiService aiService;
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public CodeExplanationResponse explainCode(CodeExplanationRequest request) {
        log.info("Explaining {} code at {} level", request.getLanguage(), request.getDetailLevel());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String explanation = aiService.callGithubInference(List.of(message), 0.2);
            
            return new CodeExplanationResponse(
                explanation,
                request.getLanguage(),
                request.getDetailLevel()
            );
            
        } catch (Exception e) {
            log.error("Code explanation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to explain code: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(CodeExplanationRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Explain this ").append(request.getLanguage()).append(" code (")
              .append(request.getDetailLevel()).append(" level):\n\n")
              .append(request.getCode()).append("\n\n");
        
        prompt.append("Include:\n");
        prompt.append("1. Purpose and functionality\n");
        prompt.append("2. Key functions/structures\n");
        prompt.append("3. Inputs and outputs\n");
        
        switch (request.getDetailLevel().toLowerCase()) {
            case "advanced":
                prompt.append("4. Complexity analysis and optimization suggestions\n");
                prompt.append("5. Potential improvements and best practices\n");
                break;
            case "intermediate":
                prompt.append("4. Key algorithms and logic flow\n");
                prompt.append("5. Common use cases and variations\n");
                break;
            case "beginner":
            default:
                prompt.append("4. Basic functionality and simple explanation\n");
                prompt.append("5. What each part does in simple terms\n");
                break;
        }
        
        return prompt.toString();
    }
}
