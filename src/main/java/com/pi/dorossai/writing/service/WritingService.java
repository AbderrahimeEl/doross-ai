package com.pi.dorossai.writing.service;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.writing.dto.WritingImprovementRequest;
import com.pi.dorossai.writing.dto.WritingImprovementResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class WritingService {
    
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public WritingImprovementResponse improveWriting(WritingImprovementRequest request) {
        log.info("Improving writing in {} style for text of length: {}", request.getStyle(), request.getText().length());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.5);
            
            // Parse JSON response
            WritingImprovementResponse result = parseAiResponse(response, request);
            
            return result;
            
        } catch (Exception e) {
            log.error("Writing improvement failed: {}", e.getMessage());
            throw new RuntimeException("Failed to improve writing: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(WritingImprovementRequest request) {
        return String.format(
            "Rewrite the following text in %s style:\n\n" +
            "Original: %s\n\n" +
            "Please provide your response in JSON format with the following structure:\n" +
            "{\n" +
            "  \"improved_text\": \"your improved version here\",\n" +
            "  \"changes\": [\"list of specific changes made\"],\n" +
            "  \"original_length\": %d,\n" +
            "  \"improved_length\": 0\n" +
            "}\n\n" +
            "Focus on:\n" +
            "- Clarity and readability\n" +
            "- Appropriate tone for %s style\n" +
            "- Grammar and structure improvements\n" +
            "- Maintaining the original meaning",
            request.getStyle(),
            request.getText(),
            request.getText().length(),
            request.getStyle()
        );
    }
    
    private WritingImprovementResponse parseAiResponse(String response, WritingImprovementRequest request) {
        try {
            // Try to parse as JSON first
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);
            
            String improvedText = (String) jsonResponse.get("improved_text");
            List<String> changes = (List<String>) jsonResponse.get("changes");
            Integer originalLength = (Integer) jsonResponse.getOrDefault("original_length", request.getText().length());
            Integer improvedLength = improvedText != null ? improvedText.length() : 0;
            
            return new WritingImprovementResponse(
                improvedText,
                changes,
                originalLength,
                improvedLength
            );
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating fallback response: {}", e.getMessage());
            
            // Fallback: treat the entire response as improved text
            return new WritingImprovementResponse(
                response,
                List.of("Applied " + request.getStyle() + " style improvements"),
                request.getText().length(),
                response.length()
            );
        }
    }
}
