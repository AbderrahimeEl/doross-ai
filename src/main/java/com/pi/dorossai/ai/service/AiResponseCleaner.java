package com.pi.dorossai.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for cleaning AI service responses.
 * Handles removal of markdown code blocks and other formatting issues.
 */
@Component
@Slf4j
public class AiResponseCleaner {
      /**
     * Cleans AI response by removing markdown code blocks and extra formatting
     * @param response The raw AI response
     * @return Cleaned response ready for JSON parsing
     */
    public String cleanJsonResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            log.warn("Received null or empty response from AI service");
            return "{}";
        }
        
        String cleaned = response.trim();
        
        // Remove markdown code blocks
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
            log.debug("Removed ```json prefix from response");
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
            log.debug("Removed ``` prefix from response");
        }
        
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
            log.debug("Removed ``` suffix from response");
        }
        
        // Remove any extra whitespace
        cleaned = cleaned.trim();
        
        // Handle cases where AI adds explanatory text before/after JSON
        cleaned = extractJsonFromText(cleaned);
        
        log.debug("Cleaned AI response from {} characters to {} characters", 
                response.length(), cleaned.length());
        
        return cleaned;
    }
    
    /**
     * Creates a fallback JSON response for timeout scenarios
     * @param errorMessage The error message to include
     * @return A valid JSON string with error information
     */
    public String createTimeoutFallbackJson(String errorMessage) {
        return String.format("{\"error\": \"timeout\", \"message\": \"%s\"}", 
                errorMessage.replace("\"", "\\\""));
    }
    
    /**
     * Extracts JSON content from text that may contain explanatory content
     * @param text The text that contains JSON
     * @return Extracted JSON string
     */
    private String extractJsonFromText(String text) {
        // Look for the first opening brace and last closing brace
        int firstBrace = text.indexOf('{');
        int lastBrace = text.lastIndexOf('}');
        
        if (firstBrace != -1 && lastBrace != -1 && firstBrace < lastBrace) {
            String jsonPart = text.substring(firstBrace, lastBrace + 1);
            log.debug("Extracted JSON from larger text response");
            return jsonPart;
        }
        
        // If no JSON structure found, return original text
        return text;
    }
    
    /**
     * Validates if a string contains valid JSON structure
     * @param text The text to validate
     * @return true if the text appears to contain JSON
     */
    public boolean isValidJsonStructure(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = text.trim();
        return (cleaned.startsWith("{") && cleaned.endsWith("}")) ||
               (cleaned.startsWith("[") && cleaned.endsWith("]"));
    }
}
