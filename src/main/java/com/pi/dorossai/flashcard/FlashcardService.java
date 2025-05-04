package com.pi.dorossai.flashcard;

import com.pi.dorossai.config.FastApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashcardService {
    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public FlashcardResponse generateFlashcards(FlashcardRequest request) {
        log.info("Generating flashcards for topic: {}", request.getTopic());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("topic", request.getTopic());
        requestMap.put("num_cards", request.getNumCards());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

        String url = fastApiConfig.getFastApiBaseUrl() + "/generate-flashcards";
        ResponseEntity<FlashcardResponse> response = restTemplate.postForEntity(
            url, 
            entity, 
            FlashcardResponse.class
        );

        return response.getBody();
    }
}