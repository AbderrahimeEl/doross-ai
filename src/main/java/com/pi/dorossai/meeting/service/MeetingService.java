package com.pi.dorossai.meeting.service;

import com.pi.dorossai.ai.service.AiService;
import com.pi.dorossai.meeting.dto.MeetingNotesRequest;
import com.pi.dorossai.meeting.dto.MeetingNotesResponse;
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
public class MeetingService {
    
    private final AiService aiService;
    private final ObjectMapper objectMapper;
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public MeetingNotesResponse processMeetingNotes(MeetingNotesRequest request) {
        log.info("Processing meeting notes for transcript of length: {}", request.getTranscript().length());
        
        try {
            String prompt = buildPrompt(request);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            String response = aiService.callGithubInference(List.of(message), 0.3);
            
            // Parse JSON response
            MeetingNotesResponse result = parseAiResponse(response);
            
            return result;
            
        } catch (Exception e) {
            log.error("Meeting notes processing failed: {}", e.getMessage());
            throw new RuntimeException("Failed to process meeting notes: " + e.getMessage(), e);
        }
    }
    
    private String buildPrompt(MeetingNotesRequest request) {
        List<String> components = new ArrayList<>();
        if (request.isIncludeActions()) {
            components.add("action items (who is responsible, what needs to be done, when it's due)");
        }
        if (request.isIncludeDecisions()) {
            components.add("key decisions made during the meeting");
        }
        
        return String.format(
            "Process the following meeting transcript and extract structured information:\n\n" +
            "Transcript: %s\n\n" +
            "Please extract the following information and provide it in JSON format:\n" +
            "{\n" +
            "  \"summary\": \"brief summary of the meeting\",\n" +
            "  \"action_items\": [\"list of action items\"],\n" +
            "  \"key_decisions\": [\"list of key decisions\"],\n" +
            "  \"follow_up_questions\": [\"list of follow-up questions\"]\n" +
            "}\n\n" +
            "Focus on extracting:\n" +
            "1. Brief summary of the meeting's main topics and outcomes\n" +
            "%s\n" +
            "3. Follow-up questions that need to be addressed\n\n" +
            "Guidelines:\n" +
            "- Be specific and actionable in action items\n" +
            "- Include responsible parties when mentioned\n" +
            "- Focus on concrete decisions, not discussions\n" +
            "- Identify questions that require further clarification",
            request.getTranscript(),
            components.isEmpty() ? "2. Relevant information as requested" : "2. " + String.join(" and ", components)
        );
    }
    
    private MeetingNotesResponse parseAiResponse(String response) {        try {
            // Try to parse as JSON first
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);
            
            String summary = (String) jsonResponse.getOrDefault("summary", "Meeting summary not available");
            @SuppressWarnings("unchecked")
            List<String> actionItems = (List<String>) jsonResponse.getOrDefault("action_items", new ArrayList<>());
            @SuppressWarnings("unchecked")
            List<String> keyDecisions = (List<String>) jsonResponse.getOrDefault("key_decisions", new ArrayList<>());
            @SuppressWarnings("unchecked")
            List<String> followUpQuestions = (List<String>) jsonResponse.getOrDefault("follow_up_questions", new ArrayList<>());
            
            return new MeetingNotesResponse(summary, actionItems, keyDecisions, followUpQuestions);
            
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON response, creating fallback response: {}", e.getMessage());
            
            // Fallback: create a basic response
            return new MeetingNotesResponse(
                response,
                List.of("Please review the processed notes for action items"),
                List.of("Key decisions noted in the processed content"),
                List.of("Follow up on items mentioned in the meeting")
            );
        }
    }
}
