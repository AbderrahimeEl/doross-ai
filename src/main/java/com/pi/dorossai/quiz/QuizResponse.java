package com.pi.dorossai.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private List<QuizQuestion> quiz;
    private String topic;
    private String difficulty;
    private String language;
}