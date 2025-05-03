package com.pi.dorossai.summarization.service;

import com.pi.dorossai.summarization.dto.SummarizationRequest;
import com.pi.dorossai.summarization.dto.SummarizationResponse;
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
public class SummarizationService {
    private static final String SUMMARIZATION_API_URL = "http://localhost:8000/summarize";
    private final RestTemplate restTemplate;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public SummarizationResponse summarizeText(SummarizationRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("text", request.getText());
        requestMap.put("language", request.getLanguage());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestMap, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            SUMMARIZATION_API_URL, 
            entity, 
            Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        SummarizationResponse summarizationResponse = new SummarizationResponse();
        summarizationResponse.setSummary((String) responseBody.get("summary"));
        summarizationResponse.setLanguage(request.getLanguage());
        summarizationResponse.setOriginalLength(request.getText().length());
        summarizationResponse.setSummaryLength(summarizationResponse.getSummary().length());
        return summarizationResponse;
    }
}
