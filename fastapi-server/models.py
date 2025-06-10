from pydantic import BaseModel
from typing import List, Dict, Optional

class SummarizationRequest(BaseModel):
    text: str
    language: str = "english"

class SummarizationResponse(BaseModel):
    summary: str
    language: str
    original_length: int
    summary_length: int

class QuizGenerationRequest(BaseModel):
    topic: str
    num_questions: int = 5
    difficulty: str = "medium"
    language: str = "english"

class QuizQuestion(BaseModel):
    question: str
    options: List[str]
    correct_answer: str
    explanation: Optional[str] = None

class QuizResponse(BaseModel):
    quiz: List[QuizQuestion]
    topic: str
    difficulty: str
    language: str

class KeyPointsRequest(BaseModel):
    text: str
    num_points: int = 5

class KeyPointsResponse(BaseModel):
    key_points: List[str]
    original_length: int

class Flashcard(BaseModel):
    term: str
    definition: str
    context: Optional[str]

class FlashcardRequest(BaseModel):
    topic: str
    num_cards: int = 10

class FlashcardResponse(BaseModel):
    flashcards: List[Flashcard]
    topic: str
    num_cards: int

class CodeExplanationRequest(BaseModel):
    code: str
    language: str = "python"
    detail_level: str = "intermediate"

class CodeExplanationResponse(BaseModel):
    explanation: str
    language: str
    detail_level: str

class MeetingNotesRequest(BaseModel):
    transcript: str
    include_actions: bool = True
    include_decisions: bool = True

class MeetingNotesResponse(BaseModel):
    summary: str
    action_items: Optional[List[str]] = None
    key_decisions: Optional[List[str]] = None
    follow_up_questions: Optional[List[str]] = None

class DocumentQARequest(BaseModel):
    question: str
    context: str

class DocumentQAResponse(BaseModel):
    answer: str
    confidence: Optional[str] = None

class WritingImprovementRequest(BaseModel):
    text: str
    style: str = "professional"

class WritingImprovementResponse(BaseModel):
    improved_text: str
    changes: List[str]
    original_length: int
    improved_length: int

class ModerationRequest(BaseModel):
    text: str