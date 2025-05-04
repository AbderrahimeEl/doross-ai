package com.pi.dorossai.flashcard;

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
public class FlashcardController {
    
    private final FlashcardService flashcardService;
    
    @PostMapping("/generate-flashcards")
    public ResponseEntity<FlashcardResponse> generateFlashcards(@Valid @RequestBody FlashcardRequest request) {
        log.info("Received request to generate flashcards on topic: {}", request.getTopic());
        FlashcardResponse response = flashcardService.generateFlashcards(request);
        return ResponseEntity.ok(response);
    }
}