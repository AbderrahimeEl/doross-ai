package com.pi.dorossai.writing.controller;

import com.pi.dorossai.writing.dto.WritingImprovementRequest;
import com.pi.dorossai.writing.dto.WritingImprovementResponse;
import com.pi.dorossai.writing.service.WritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Writing Improvement", description = "AI-powered writing enhancement services")
public class WritingController {
    
    private final WritingService writingService;
    
    @PostMapping("/improve-writing")
    @Operation(summary = "Improve Writing", description = "Enhance text quality and style using AI")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<WritingImprovementResponse> improveWriting(@Valid @RequestBody WritingImprovementRequest request) {
        log.info("Received request to improve writing in {} style for text of length: {}", 
                request.getStyle(), request.getText().length());
        
        WritingImprovementResponse response = writingService.improveWriting(request);
        return ResponseEntity.ok(response);
    }
}
