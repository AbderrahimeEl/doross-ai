package com.pi.dorossai.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentQAResponse {
    private String answer;
    private String confidence;
}
