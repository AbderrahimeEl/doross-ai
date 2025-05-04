package com.pi.dorossai.keypoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class KeyPointsController {
    
    private final KeyPointsService keyPointsService;
    
    @PostMapping("/extract-key-points")
    public ResponseEntity<KeyPointsResponse> extractKeyPoints(@Valid @RequestBody KeyPointsRequest request) {
        log.info("Received request to extract key points from text of length: {}", request.getText().length());
        KeyPointsResponse response = keyPointsService.extractKeyPoints(request);
        return ResponseEntity.ok(response);
    }
}