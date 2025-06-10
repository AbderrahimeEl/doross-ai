package com.pi.dorossai.moderation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModerationResponse {
    private boolean flagged;
    private String level;
    private List<String> categories;
    private double confidence;
    private String reason;
}
