package com.pi.dorossai.flashcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardResponse {
    private List<Flashcard> flashcards;
    private String topic;
    private int numCards;
}