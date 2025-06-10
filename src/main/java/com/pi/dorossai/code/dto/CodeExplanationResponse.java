package com.pi.dorossai.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeExplanationResponse {
    private String explanation;
    private String language;
    private String detailLevel;
}
