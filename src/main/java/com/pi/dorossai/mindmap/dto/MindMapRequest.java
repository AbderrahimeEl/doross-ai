package com.pi.dorossai.mindmap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MindMapRequest {
    @NotBlank(message = "Topic is required")
    private String topic;
}
