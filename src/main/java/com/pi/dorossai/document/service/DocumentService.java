package com.pi.dorossai.document.service;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.ai.service.AiResponseCleaner;
import com.pi.dorossai.document.dto.DocumentQARequest;
import com.pi.dorossai.document.dto.DocumentQAResponse;
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
public class DocumentService {
    
    private final AiService aiService;
    private final AiResponseCleaner responseCleaner;
    private final ObjectMapper objectMapper;
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public DocumentQAResponse askDocument(DocumentQARequest request) {
        log.info("Processing document Q&A for question: {}", request.getQuestion());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.1);
            
            // Parse JSON response
            DocumentQAResponse result = parseAiResponse(response);
            
            return result;
            
        } catch (Exception e) {
            log.error("Document Q&A failed: {}", e.getMessage());
            throw new RuntimeException("Failed to answer question: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(DocumentQARequest request) {
        return String.format(
            "Answer the following question based strictly on the provided context. " +
            "If the answer cannot be determined from the context, respond with " +
            "\"I cannot determine from the given context\".\n\n" +
            "Question: %s\n\n" +            "Context: %s\n\n" +
            "IMPORTANT: Respond ONLY with valid JSON, no markdown code blocks, no explanations, no additional text.\n\n" +
            "Required JSON format:\n" +
            "{\n" +
            "  \"answer\": \"your answer here\",\n" +
            "  \"confidence\": \"high/medium/low\"\n" +
            "}\n\n" +
            "Guidelines:\n" +            "- Only use information from the provided context\n" +
            "- Be concise and accurate\n" +
            "- Set confidence based on how clearly the context supports your answer\n" +
            "- Return valid JSON only, no markdown formatting\n" +
            "- Use 'high' confidence when the context directly answers the question\n" +
            "- Use 'medium' confidence when the answer requires some inference\n" +
            "- Use 'low' confidence when the context only partially supports the answer",
            request.getQuestion(),
            request.getContext()
        );
    }
      private DocumentQAResponse parseAiResponse(String response) {        
        try {
            // Clean the response using the centralized cleaner
            String cleanedResponse = responseCleaner.cleanJsonResponse(response);
            
            // Try to parse as JSON first
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            
            String answer = (String) jsonResponse.get("answer");
            String confidence = (String) jsonResponse.getOrDefault("confidence", "medium");
            
            return new DocumentQAResponse(answer, confidence);
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating fallback response: {}", e.getMessage());
            
            // Fallback: treat the entire response as the answer
            return new DocumentQAResponse(response, "medium");
        }
    }
}
