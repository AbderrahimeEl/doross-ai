package com.pi.dorossai.summarization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing the summarized text with length statistics")
public class SummarizationResponse {
    @Schema(
        description = "The generated summary of the input text",
        example = "Artificial Intelligence (AI) refers to the simulation of human intelligence in machines programmed to think and learn. AI systems can perform tasks that typically require human intelligence, such as visual perception, speech recognition, and decision-making. Machine learning, a subset of AI, enables systems to automatically learn and improve from experience without being explicitly programmed."
    )
    private String summary;
    
    @Schema(
        description = "Language of the generated summary",
        example = "english"
    )
    private String language;
    
    @Schema(
        description = "Character count of the original text",
        example = "1250"
    )
    private int originalLength;
    
    @Schema(
        description = "Character count of the generated summary",
        example = "387"
    )
    private int summaryLength;
}