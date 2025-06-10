package com.pi.dorossai.document.controller;

import com.pi.dorossai.document.dto.DocumentQARequest;
import com.pi.dorossai.document.dto.DocumentQAResponse;
import com.pi.dorossai.document.service.DocumentService;
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
@Tag(name = "Document Q&A", description = "Document-based question answering services")
public class DocumentController {
    
    private final DocumentService documentService;
    
    @PostMapping("/ask-document")
    @Operation(summary = "Ask Document", description = "Answer questions based on provided document context")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<DocumentQAResponse> askDocument(@Valid @RequestBody DocumentQARequest request) {
        log.info("Received document Q&A request for question: {}", request.getQuestion());
        
        DocumentQAResponse response = documentService.askDocument(request);
        return ResponseEntity.ok(response);
    }
}
