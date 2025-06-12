package com.pi.dorossai.mindmap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.pi.dorossai.ai.service.AiService;

/**
 * Service for generating mind maps in Mermaid format.
 * Uses AI to create hierarchical concept maps from topics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MindMapService {
    private final AiService aiService;    /**
     * Generates a mind map in Mermaid format for the provided topic.
     * The mind map is structured as a graph with nodes representing concepts
     * and edges representing relationships between concepts.
     *
     * The format follows the Mermaid graph TD (top-down) syntax, with each line
     * defining a relationship between two nodes. For example:
     * 
     * graph TD;
     * A[Computer Science] --> B[Programming]
     * A --> C[Data Structures]
     * B --> B1[Languages]
     * B1 --> B1a[Python]
     * 
     * The resulting mind maps typically have 3-4 levels of hierarchy depending on
     * the complexity of the topic.
     *
     * @param topic The topic for which to generate a mind map
     * @return A string containing the Mermaid format diagram code
     */
    public String generateMindMapMermaid(String topic) {
        log.info("Generating mind map for topic: {}", topic);
        
        // Compose a prompt for the AI to generate a mind map in Mermaid format
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a comprehensive mind map in Mermaid format for the following topic. ");
        prompt.append("The mind map should show the main concept, key sub-topics, and details for each sub-topic. ");
        prompt.append("Use a hierarchical structure with meaningful node labels. ");
        prompt.append("Return only the Mermaid code, no explanation.\n\n");
        prompt.append("Topic: ").append(topic).append("\n\n");
        prompt.append("Format requirements:\n");
        prompt.append("1. Use 'graph TD;' syntax (top-down graph)\n");
        prompt.append("2. Use descriptive node IDs (e.g., 'MachineLearning' not just 'A')\n");
        prompt.append("3. Use square brackets for node labels [Like This]\n");
        prompt.append("4. Include at least 3 levels of hierarchy when appropriate\n");
        prompt.append("5. Return ONLY valid Mermaid code, no explanation, no markdown, no code blocks\n\n");
        prompt.append("Example structure (but more detailed):\n");
        prompt.append("graph TD;\n");
        prompt.append("  MainTopic[Main Topic] --> SubTopic1[Sub-Topic 1];\n");
        prompt.append("  MainTopic --> SubTopic2[Sub-Topic 2];\n");
        prompt.append("  SubTopic1 --> Detail1[Detail 1];\n");
        prompt.append("  SubTopic1 --> Detail2[Detail 2];\n");

        // Prepare the message for the AI service
        var message = java.util.Map.of(
            "role", "user",
            "content", prompt.toString()
        );
        var messages = java.util.List.of(message);

        // Call the AI service (temperature can be adjusted as needed)
        log.debug("Sending request to AI service");
        String aiResponse = aiService.callGithubInference(messages, 0.7);
        log.debug("Received response from AI service");

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
        
        log.info("Mind map generated successfully with {} characters", result.length());
        return result;
    }
}
