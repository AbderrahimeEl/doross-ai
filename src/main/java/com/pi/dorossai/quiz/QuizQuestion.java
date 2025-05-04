package com.pi.dorossai.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestion {
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String explanation;
}