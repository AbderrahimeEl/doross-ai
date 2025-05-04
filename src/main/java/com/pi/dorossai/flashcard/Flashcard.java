package com.pi.dorossai.flashcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flashcard {
    private String term;
    private String definition;
    private String context;
}