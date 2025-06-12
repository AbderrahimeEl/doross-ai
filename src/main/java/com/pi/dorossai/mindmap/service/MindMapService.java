package com.pi.dorossai.mindmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pi.dorossai.ai.service.AiService;

@Service
@RequiredArgsConstructor
public class MindMapService {
    private final AiService aiService;

    public String generateMindMapMermaid(String topic) {
        // Compose a prompt for the AI to generate a mind map in Mermaid format
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a mind map in Mermaid format for the following topic. ");
        prompt.append("Return only the Mermaid code, no explanation.\n");
        prompt.append("Topic: ").append(topic).append("\n");
        prompt.append("Format: graph TD; ...\n");
        prompt.append("Return ONLY valid Mermaid code, no explanation, no markdown, no formatting, no extra text. Do not wrap in code blocks.\n");

        // Prepare the message for the AI service
        var message = java.util.Map.of(
            "role", "user",
            "content", prompt.toString()
        );
        var messages = java.util.List.of(message);

        // Call the AI service (temperature can be adjusted as needed)
        String aiResponse = aiService.callGithubInference(messages, 0.7);

        // Extract only the valid Mermaid code (lines starting with 'graph')
        StringBuilder mermaid = new StringBuilder();
        boolean inMermaid = false;
        for (String line : aiResponse.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("graph ")) {
                inMermaid = true;
            }
            if (inMermaid) {
                if (!trimmed.isEmpty()) {
                    mermaid.append(trimmed).append("\n");
                }
            }
        }
        String result = mermaid.toString().trim();
        return result;
    }
}
