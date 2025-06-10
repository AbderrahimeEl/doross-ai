from fastapi import HTTPException
import json
import logging
from models import QuizGenerationRequest, QuizResponse, QuizQuestion
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def generate_quiz(quiz_request: QuizGenerationRequest) -> QuizResponse:
    if quiz_request.num_questions < 1 or quiz_request.num_questions > 20:
        raise HTTPException(status_code=400, detail="Number of questions must be between 1 and 20")

    try:
        system_prompt = f"""Generate a {quiz_request.difficulty} quiz about {quiz_request.topic} with {quiz_request.num_questions} questions.
        For each question provide:
        - Clear question
        - 4 options (labeled A-D)
        - Correct answer (letter only)
        - Brief explanation
        
        Return JSON format: {{"questions": [{{"question": "...", "options": ["A...", ...], "correct_answer": "A", "explanation": "..."}}]}}"""
        
        quiz_json_str = await call_github_inference([
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": f"Topic: {quiz_request.topic}"}
        ], temperature=0.5)
        
        quiz_data = json.loads(quiz_json_str)
        return QuizResponse(
            quiz=[QuizQuestion(**q) for q in quiz_data["questions"]],
            topic=quiz_request.topic,
            difficulty=quiz_request.difficulty,
            language=quiz_request.language
        )
    except json.JSONDecodeError:
        raise HTTPException(status_code=500, detail="Failed to parse quiz response")
    except Exception as e:
        logger.error(f"Quiz generation failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to generate quiz")