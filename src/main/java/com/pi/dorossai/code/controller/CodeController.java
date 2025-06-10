package com.pi.dorossai.code.controller;

import com.pi.dorossai.code.dto.CodeExplanationRequest;
import com.pi.dorossai.code.dto.CodeExplanationResponse;
import com.pi.dorossai.code.service.CodeService;
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
@Tag(name = "Code Analysis", description = "Code explanation and analysis services")
public class CodeController {
    
    private final CodeService codeService;
    
    @PostMapping("/explain-code")
    @Operation(summary = "Explain Code", description = "Analyze and explain code functionality at different detail levels")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<CodeExplanationResponse> explainCode(@Valid @RequestBody CodeExplanationRequest request) {
        log.info("Received request to explain {} code at {} level", request.getLanguage(), request.getDetailLevel());
        
        CodeExplanationResponse response = codeService.explainCode(request);
        return ResponseEntity.ok(response);
    }
}
