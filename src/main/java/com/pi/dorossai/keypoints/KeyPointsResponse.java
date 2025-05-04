package com.pi.dorossai.keypoints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyPointsResponse {
    private List<String> keyPoints;
    private int originalLength;
}