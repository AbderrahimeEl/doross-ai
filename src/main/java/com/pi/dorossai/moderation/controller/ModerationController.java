package com.pi.dorossai.moderation.controller;

import com.pi.dorossai.moderation.dto.ModerationRequest;
import com.pi.dorossai.moderation.dto.ModerationResponse;
import com.pi.dorossai.moderation.service.ModerationService;
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
@Tag(name = "Content Moderation", description = "AI-powered content safety and moderation services")
public class ModerationController {
    
    private final ModerationService moderationService;
    
    @PostMapping("/moderate-content")
    @Operation(summary = "Moderate Content", description = "Analyze text for harmful or inappropriate content")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ModerationResponse> moderateContent(@Valid @RequestBody ModerationRequest request) {
        log.info("Received content moderation request at {} level for text of length: {}", 
                request.getLevel(), request.getText().length());
        
        ModerationResponse response = moderationService.moderateContent(request);
        return ResponseEntity.ok(response);
    }
}
