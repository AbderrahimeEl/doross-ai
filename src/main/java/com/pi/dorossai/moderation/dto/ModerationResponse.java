package com.pi.dorossai.moderation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing moderation results with detailed safety analysis")
public class ModerationResponse {
    @Schema(
        description = "Whether the content was flagged as potentially harmful",
        example = "false"
    )
    private boolean flagged;
    
    @Schema(
        description = "The moderation level used for analysis",
        example = "standard"
    )
    private String level;
    
    @Schema(
        description = "List of specific categories that triggered moderation flags",
        example = "[\"harassment\", \"hate-speech\"]"
    )
    private List<String> categories;
    
    @Schema(
        description = "Confidence score of the moderation decision (0.0 to 1.0)",
        example = "0.95",
        minimum = "0.0",
        maximum = "1.0"
    )
    private double confidence;
    
    @Schema(
        description = "Detailed explanation of why content was flagged (empty if safe)",
        example = "Content contains language that could be considered harassment and includes hate speech targeting a specific group."
    )
    private String reason;
}
