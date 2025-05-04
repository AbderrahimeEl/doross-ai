package com.pi.dorossai.summarization;

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
public class SummarizationController {
    
    private final SummarizationService summarizationService;
    
    @PostMapping("/summarize")
    public ResponseEntity<SummarizationResponse> summarizeText(@Valid @RequestBody SummarizationRequest request) {
        SummarizationResponse response = summarizationService.summarizeText(request);
        return ResponseEntity.ok(response);
    }
}