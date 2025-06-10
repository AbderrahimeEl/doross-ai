from fastapi import HTTPException
import json
import logging
from models import FlashcardRequest, FlashcardResponse, Flashcard
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def generate_flashcards(request: FlashcardRequest) -> FlashcardResponse:
    """Generate flashcards for a given topic"""
    try:
        prompt = f"""Create {request.num_cards} flashcards about {request.topic}.
        Each flashcard should have:
        - Term/concept name
        - Concise definition
        - Optional example/context
        
        Return as JSON: {{"flashcards": [{{"term": "...", "definition": "...", "context": "..."}}]}}"""
        
        response = await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.4
        )
        flashcards_data = json.loads(response)
        return FlashcardResponse(
            flashcards=[Flashcard(**card) for card in flashcards_data["flashcards"]],
            topic=request.topic,
            num_cards=request.num_cards
        )
    except Exception as e:
        logger.error(f"Flashcard generation failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to generate flashcards")