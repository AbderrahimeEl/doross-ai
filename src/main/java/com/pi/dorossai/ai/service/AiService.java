package com.pi.dorossai.ai.service;

import com.pi.dorossai.ai.config.AiServiceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    
    @Qualifier("aiRestTemplate")
    private final RestTemplate restTemplate;
    private final AiServiceConfig aiServiceConfig;
    
    private static final String GITHUB_ENDPOINT = "https://models.inference.ai.azure.com/chat/completions";
    private static final String MODEL = "gpt-4o";
    
    public String callGithubInference(List<Map<String, String>> messages, double temperature) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(aiServiceConfig.getGithubApiKey());
            headers.set("Accept", "application/json");
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("messages", messages);
            payload.put("temperature", temperature);
            payload.put("model", MODEL);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            
            log.info("Sending request to GitHub AI endpoint");
            ResponseEntity<Map> response = restTemplate.postForEntity(GITHUB_ENDPOINT, entity, Map.class);
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("choices")) {
                throw new RuntimeException("Invalid response format - missing choices");
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new RuntimeException("Empty choices in response");
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
            
        } catch (Exception e) {
            log.error("AI service call failed: {}", e.getMessage());
            throw new RuntimeException("Failed to call AI service: " + e.getMessage(), e);
        }
    }
}
