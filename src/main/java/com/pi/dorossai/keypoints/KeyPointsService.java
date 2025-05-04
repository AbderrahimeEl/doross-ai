package com.pi.dorossai.keypoints;

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
public class KeyPointsService {
    private final RestTemplate restTemplate;
    private final FastApiConfig fastApiConfig;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public KeyPointsResponse extractKeyPoints(KeyPointsRequest request) {
        log.info("Extracting key points from text of length: {}", request.getText().length());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("text", request.getText());
        requestMap.put("num_points", request.getNumPoints());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

        String url = fastApiConfig.getFastApiBaseUrl() + "/extract-key-points";
        ResponseEntity<KeyPointsResponse> response = restTemplate.postForEntity(
            url, 
            entity, 
            KeyPointsResponse.class
        );

        return response.getBody();
    }
}