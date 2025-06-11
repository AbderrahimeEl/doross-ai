package com.pi.dorossai.moderation.service;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.ai.service.AiResponseCleaner;
import com.pi.dorossai.moderation.dto.ModerationRequest;
import com.pi.dorossai.moderation.dto.ModerationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationService {
    
    private final AiService aiService;
    private final AiResponseCleaner responseCleaner;
    private final ObjectMapper objectMapper;
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public ModerationResponse moderateContent(ModerationRequest request) {
        log.info("Moderating content at {} level for text of length: {}", request.getLevel(), request.getText().length());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.1);
            
            // Parse JSON response
            ModerationResponse result = parseAiResponse(response, request);
            
            return result;
            
        } catch (Exception e) {
            log.error("Content moderation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to moderate content: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(ModerationRequest request) {
        String threshold = switch (request.getLevel().toLowerCase()) {
            case "strict" -> "very low tolerance for any potentially inappropriate content";
            case "lenient" -> "only flag clearly harmful or dangerous content";
            default -> "standard moderation guidelines";
        };
        
        return String.format(
            "Analyze the following text for potentially harmful or inappropriate content using %s:\n\n" +            "Text to analyze: %s\n\n" +
            "IMPORTANT: Respond ONLY with valid JSON, no markdown code blocks, no explanations, no additional text.\n\n" +
            "Required JSON format:\n" +
            "{\n" +
            "  \"flagged\": boolean,\n" +
            "  \"level\": \"%s\",\n" +
            "  \"categories\": [\"list of categories if flagged: harassment, hate-speech, violence, sexual, self-harm, illegal\"],\n" +
            "  \"confidence\": 0.0-1.0,\n" +
            "  \"reason\": \"explanation if flagged, empty if safe\"\n" +
            "}\n\n" +
            "Categories to check:\n" +
            "- Harassment or bullying\n" +
            "- Hate speech or discrimination\n" +
            "- Violence or threats\n" +
            "- Sexual content\n" +
            "- Self-harm or suicide\n" +            "- Illegal activities\n" +
            "- Spam or misleading information\n\n" +
            "Return valid JSON only, no markdown formatting.\n" +
            "Return confidence score (0.0-1.0) indicating how certain you are about the classification.",
            threshold,
            request.getText(),
            request.getLevel()
        );
    }
      private ModerationResponse parseAiResponse(String response, ModerationRequest request) {        
        try {
            // Clean the response using the centralized cleaner
            String cleanedResponse = responseCleaner.cleanJsonResponse(response);
            
            // Try to parse as JSON first
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            
            boolean flagged = (Boolean) jsonResponse.getOrDefault("flagged", false);
            String level = (String) jsonResponse.getOrDefault("level", request.getLevel());
            @SuppressWarnings("unchecked")
            List<String> categories = (List<String>) jsonResponse.getOrDefault("categories", new ArrayList<>());
            Object confidenceObj = jsonResponse.getOrDefault("confidence", 0.5);
            double confidence = confidenceObj instanceof Number ? ((Number) confidenceObj).doubleValue() : 0.5;
            String reason = (String) jsonResponse.getOrDefault("reason", "");
            
            return new ModerationResponse(flagged, level, categories, confidence, reason);
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating safe fallback response: {}", e.getMessage());
            
            // Fallback: assume content is safe if we can't parse the response
            return new ModerationResponse(
                false,
                request.getLevel(),
                new ArrayList<>(),
                0.5,
                "Analysis completed"
            );
        }
    }
}
