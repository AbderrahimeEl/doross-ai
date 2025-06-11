package com.pi.dorossai.keypoints;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to extract key points from text content")
public class KeyPointsRequest {
    @NotBlank
    @Schema(description = "Text content to analyze for key points", example = "Artificial Intelligence is transforming industries...", required = true)
    private String text;
    
    @Min(1)
    @Max(20)
    @Schema(description = "Number of key points to extract", example = "5", defaultValue = "5", minimum = "1", maximum = "20")
    private int numPoints = 5;
}