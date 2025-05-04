package com.pi.dorossai.quiz;

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
public class QuizController {
    
    private final QuizService quizService;
    
    @PostMapping("/generate-quiz")
    public ResponseEntity<QuizResponse> generateQuiz(@Valid @RequestBody QuizGenerationRequest request) {
        log.info("Received request to generate quiz on topic: {}", request.getTopic());
        QuizResponse response = quizService.generateQuiz(request);
        return ResponseEntity.ok(response);
    }
}