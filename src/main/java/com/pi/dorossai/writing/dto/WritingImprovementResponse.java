package com.pi.dorossai.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WritingImprovementResponse {
    private String improvedText;
    private List<String> changes;
    private int originalLength;
    private int improvedLength;
}
