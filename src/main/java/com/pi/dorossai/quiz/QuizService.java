package com.pi.dorossai.quiz;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.ai.service.AiResponseCleaner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
    
    private final AiService aiService;
    private final AiResponseCleaner responseCleaner;
    private final ObjectMapper objectMapper;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public QuizResponse generateQuiz(QuizGenerationRequest request) {
        log.info("Generating quiz for topic: {}", request.getTopic());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.6);
            
            // Parse JSON response
            QuizResponse result = parseAiResponse(response, request);
            
            return result;
            
        } catch (Exception e) {
            log.error("Quiz generation failed: {}", e.getMessage());
            throw new RuntimeException("Failed to generate quiz: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(QuizGenerationRequest request) {
        return String.format(
            "Generate a %s quiz with %d questions on the topic: %s\n" +
            "Difficulty level: %s\n" +            "Language: %s\n\n" +
            "IMPORTANT: Respond ONLY with valid JSON, no markdown code blocks, no explanations, no additional text.\n\n" +
            "Required JSON format:\n" +
            "{\n" +
            "  \"questions\": [\n" +
            "    {\n" +
            "      \"question\": \"Question text\",\n" +
            "      \"options\": [\"A\", \"B\", \"C\", \"D\"],\n" +
            "      \"correctAnswer\": \"A\",\n" +
            "      \"explanation\": \"Why this is correct\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"topic\": \"%s\",\n" +
            "  \"difficulty\": \"%s\",\n" +
            "  \"numQuestions\": %d\n" +
            "}\n\n" +
            "Guidelines:\n" +            "- Make questions appropriate for %s difficulty level\n" +
            "- Provide 4 multiple choice options for each question\n" +
            "- Include clear explanations for correct answers\n" +
            "- Return valid JSON only, no markdown formatting\n" +
            "- Ensure questions test understanding, not just memorization",
            request.getDifficulty(),
            request.getNumQuestions(),
            request.getTopic(),
            request.getDifficulty(),
            request.getLanguage(),
            request.getTopic(),
            request.getDifficulty(),
            request.getNumQuestions(),
            request.getDifficulty()
        );
    }
      private QuizResponse parseAiResponse(String response, QuizGenerationRequest request) {
        try {
            // Clean the response using the centralized cleaner
            String cleanedResponse = responseCleaner.cleanJsonResponse(response);
            
            // Try to parse as JSON first
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(cleanedResponse, Map.class);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> questionsData = (List<Map<String, Object>>) jsonResponse.get("questions");
            
            List<QuizQuestion> questions = new ArrayList<>();
            if (questionsData != null) {
                for (Map<String, Object> qData : questionsData) {
                    @SuppressWarnings("unchecked")
                    List<String> options = (List<String>) qData.get("options");
                    questions.add(new QuizQuestion(
                        (String) qData.get("question"),
                        options != null ? options : List.of("A", "B", "C", "D"),
                        (String) qData.get("correctAnswer"),
                        (String) qData.get("explanation")
                    ));
                }
            }
              return new QuizResponse(
                questions,
                request.getTopic(),
                request.getDifficulty(),
                request.getLanguage()
            );
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating fallback quiz: {}", e.getMessage());
            
            // Fallback: create a single question with the response
            List<QuizQuestion> fallbackQuestions = List.of(
                new QuizQuestion(
                    "What can you tell me about " + request.getTopic() + "?",
                    List.of("Option A", "Option B", "Option C", "Option D"),
                    "Option A",
                    response
                )
            );
              return new QuizResponse(
                fallbackQuestions,
                request.getTopic(),
                request.getDifficulty(),
                request.getLanguage()
            );
        }
    }
}