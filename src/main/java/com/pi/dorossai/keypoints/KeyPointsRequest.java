package com.pi.dorossai.keypoints;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyPointsRequest {
    @NotBlank
    private String text;
    
    @Min(1)
    @Max(20)
    private int numPoints = 5;
}