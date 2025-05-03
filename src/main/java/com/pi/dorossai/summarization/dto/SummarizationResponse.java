package com.pi.dorossai.summarization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummarizationResponse {
    private String summary;
    private String language;
    private int originalLength;
    private int summaryLength;
}