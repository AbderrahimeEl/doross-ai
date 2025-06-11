package com.pi.dorossai.flashcard;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.ai.service.AiResponseCleaner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashcardService {
    
    private final AiService aiService;
    private final AiResponseCleaner responseCleaner;
    private final ObjectMapper objectMapper;    @Retryable(
        value = { Exception.class },
        exclude = { ResourceAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public FlashcardResponse generateFlashcards(FlashcardRequest request) {
        log.info("Generating flashcards for topic: {}", request.getTopic());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.7);
            
            // Parse JSON response
            FlashcardResponse result = parseAiResponse(response, request);
              return result;
              } catch (ResourceAccessException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Read timed out")) {
                log.error("Flashcard generation timed out: {}", e.getMessage());
                // Return a fallback response for timeout
                List<Flashcard> fallbackCards = List.of(
                    new Flashcard(
                        "Service Timeout",
                        "The flashcard generation service is currently experiencing high load. Please try again in a moment."
                    )
                );
                return new FlashcardResponse(fallbackCards, request.getTopic(), 1);
            } else {
                log.error("Flashcard generation connection failed: {}", e.getMessage());
                throw new RuntimeException("Failed to connect to AI service: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Flashcard generation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to generate flashcards: " + e.getMessage(), e);
        }
    }
      private String buildPrompt(FlashcardRequest request) {
        return String.format(
            "Generate %d educational flashcards on the topic: %s\n\n" +
            "IMPORTANT: Respond ONLY with valid JSON, no markdown, no explanations, no additional text.\n\n" +
            "Required JSON format:\n" +
            "{\n" +
            "  \"flashcards\": [\n" +
            "    {\n" +
            "      \"question\": \"Question text\",\n" +
            "      \"answer\": \"Answer text\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"topic\": \"%s\",\n" +
            "  \"numCards\": %d\n" +
            "}\n\n" +
            "Guidelines:\n" +
            "- Make questions clear and educational\n" +
            "- Provide comprehensive but concise answers\n" +
            "- Cover different aspects of the topic\n" +
            "- Return valid JSON only, no markdown formatting\n" +
            "- Ensure questions test understanding, not just memorization",
            request.getNumCards(),
            request.getTopic(),
            request.getTopic(),
            request.getNumCards()
        );
    }      private FlashcardResponse parseAiResponse(String response, FlashcardRequest request) {
        try {
            // Clean the response using the centralized cleaner
            String cleanedResponse = responseCleaner.cleanJsonResponse(response);
            
            // Try to parse as JSON
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            
            @SuppressWarnings("unchecked")
            List<Map<String, String>> flashcardData = (List<Map<String, String>>) jsonResponse.get("flashcards");
            
            List<Flashcard> flashcards = new ArrayList<>();
            if (flashcardData != null) {
                for (Map<String, String> card : flashcardData) {
                    flashcards.add(new Flashcard(
                        card.get("question"),
                        card.get("answer")
                    ));
                }
            }
            
            return new FlashcardResponse(
                flashcards,
                request.getTopic(),
                flashcards.size()
            );
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating fallback flashcards: {}", e.getMessage());
            
            // Fallback: create a single flashcard with the response
            List<Flashcard> fallbackCards = List.of(
                new Flashcard(
                    "What can you tell me about " + request.getTopic() + "?",
                    "Please try the request again for better formatted response."
                )
            );
            
            return new FlashcardResponse(
                fallbackCards,
                request.getTopic(),
                1
            );
        }
    }
}