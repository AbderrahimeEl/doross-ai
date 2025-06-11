package com.pi.dorossai.keypoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi.dorossai.ai.service.AiResponseCleaner;
import com.pi.dorossai.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyPointsService {
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final AiResponseCleaner aiResponseCleaner;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public KeyPointsResponse extractKeyPoints(KeyPointsRequest request) {
        log.info("Extracting key points from text of length: {} with {} points requested", 
                request.getText().length(), request.getNumPoints());
        
        try {
            String prompt = String.format("""
                Extract exactly %d key points from the following text. Return them as a JSON response with the following structure:
                {
                  "keyPoints": ["point 1", "point 2", ...],
                  "originalLength": %d
                }
                
                Requirements:
                - Extract the most important and meaningful points
                - Each point should be concise and clear (1-2 sentences max)
                - Points should be distinct and non-overlapping
                - Maintain the original meaning and context
                - Return exactly %d points
                
                IMPORTANT: Respond ONLY with valid JSON, no markdown code blocks, no additional text.
                
                Text to analyze:
                %s
                """, 
                request.getNumPoints(), 
                request.getText().length(),
                request.getNumPoints(),
                request.getText());            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
              String aiResponse = aiService.callGithubInference(List.of(message), 0.3);
            String cleanedResponse = aiResponseCleaner.cleanJsonResponse(aiResponse);
            
            log.debug("AI response for key points extraction: {}", cleanedResponse);
            
            // Parse the JSON response
            return parseAiResponse(cleanedResponse, request);
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response from AI service: {}", e.getMessage());
            log.debug("Raw AI response that failed to parse: {}", e.getMessage());
            return createFallbackResponse(request);
        } catch (ResourceAccessException e) {
            log.warn("Request timeout while extracting key points: {}", e.getMessage());
            return createTimeoutFallbackResponse(request);
        } catch (Exception e) {
            log.error("Error extracting key points", e);
            return createFallbackResponse(request);
        }
    }

    private KeyPointsResponse parseAiResponse(String cleanedResponse, KeyPointsRequest request) throws JsonProcessingException {
        Map<String, Object> responseMap = objectMapper.readValue(cleanedResponse, 
                new TypeReference<Map<String, Object>>() {});
        
        @SuppressWarnings("unchecked")
        List<String> keyPoints = (List<String>) responseMap.get("keyPoints");
        Integer originalLength = (Integer) responseMap.get("originalLength");
        
        if (keyPoints == null || keyPoints.isEmpty()) {
            log.warn("AI response did not contain valid key points, using fallback");
            return createFallbackResponse(request);
        }
        
        KeyPointsResponse response = new KeyPointsResponse(keyPoints, 
                originalLength != null ? originalLength : request.getText().length());
        
        log.info("Successfully extracted {} key points", response.getKeyPoints().size());
        return response;
    }

    private KeyPointsResponse createFallbackResponse(KeyPointsRequest request) {
        // Create basic key points by splitting text into sentences and taking the first few
        String[] sentences = request.getText().split("[.!?]+");
        List<String> fallbackPoints = Arrays.stream(sentences)
                .limit(request.getNumPoints())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        
        if (fallbackPoints.isEmpty()) {
            fallbackPoints = List.of("Unable to extract key points from the provided text.");
        }
        
        return new KeyPointsResponse(fallbackPoints, request.getText().length());
    }

    private KeyPointsResponse createTimeoutFallbackResponse(KeyPointsRequest request) {
        List<String> timeoutMessage = List.of(
                "The AI service is currently taking longer than expected to process your request.",
                "Please try again in a moment with a shorter text or fewer key points."
        );
        return new KeyPointsResponse(timeoutMessage, request.getText().length());
    }
}