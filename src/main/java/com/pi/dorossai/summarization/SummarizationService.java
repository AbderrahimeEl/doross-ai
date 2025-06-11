package com.pi.dorossai.summarization;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.ai.service.AiResponseCleaner;
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
public class SummarizationService {
    
    private final AiService aiService;
    private final AiResponseCleaner responseCleaner;
    private final ObjectMapper objectMapper;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public SummarizationResponse summarizeText(SummarizationRequest request) {
        log.info("Summarizing text of length: {}", request.getText().length());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.3);
            
            // Parse JSON response
            SummarizationResponse result = parseAiResponse(response, request);
            
            return result;
            
        } catch (Exception e) {
            log.error("Text summarization failed: {}", e.getMessage());
            throw new RuntimeException("Failed to summarize text: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(SummarizationRequest request) {
        return String.format(
            "Summarize the following text in %s:\n\n" +            "Text: %s\n\n" +
            "IMPORTANT: Respond ONLY with valid JSON, no markdown code blocks, no explanations, no additional text.\n\n" +
            "Required JSON format:\n" +
            "{\n" +
            "  \"summary\": \"your summary here\",\n" +
            "  \"originalLength\": %d,\n" +
            "  \"summaryLength\": 0\n" +
            "}\n\n" +
            "Guidelines:\n" +            "- Keep the summary concise but comprehensive\n" +
            "- Maintain the key points and important details\n" +
            "- Use clear and accessible language\n" +
            "- Return valid JSON only, no markdown formatting\n" +
            "- Ensure the summary is in %s language",
            request.getLanguage(),
            request.getText(),
            request.getText().length(),
            request.getLanguage()
        );
    }
      private SummarizationResponse parseAiResponse(String response, SummarizationRequest request) {
        try {
            // Clean the response using the centralized cleaner
            String cleanedResponse = responseCleaner.cleanJsonResponse(response);
            
            // Try to parse as JSON first
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            
            String summary = (String) jsonResponse.get("summary");
            
            SummarizationResponse result = new SummarizationResponse();
            result.setSummary(summary);
            result.setLanguage(request.getLanguage());
            result.setOriginalLength(request.getText().length());
            result.setSummaryLength(summary != null ? summary.length() : 0);
            
            return result;
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, using fallback summary: {}", e.getMessage());
            
            // Fallback: treat the entire response as summary
            SummarizationResponse result = new SummarizationResponse();
            result.setSummary(response);
            result.setLanguage(request.getLanguage());
            result.setOriginalLength(request.getText().length());
            result.setSummaryLength(response.length());
            
            return result;
        }
    }
}
