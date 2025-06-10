from fastapi import FastAPI, Request
from slowapi import Limiter
from slowapi.util import get_remote_address
from services.audio_service import text_to_speech
from fastapi.responses import JSONResponse
from fastapi.staticfiles import StaticFiles
from models import *
from services.summarization_service import summarize_text
from services.quiz_service import generate_quiz
from services.keypoints_service import extract_key_points
from services.flashcards_service import generate_flashcards
from services.code_service import explain_code
from services.meeting_service import process_meeting_notes
from services.document_service import ask_document
from services.writing_service import improve_writing
from services.moderation_service import moderate_content

# Initialize FastAPI with rate limiting
limiter = Limiter(key_func=get_remote_address)
app = FastAPI()
app.state.limiter = limiter
app.mount("/static", StaticFiles(directory="static"), name="static")

@app.post("/summarize", response_model=SummarizationResponse)
@limiter.limit("10/minute")
async def summarize(request: Request, summarization_request: SummarizationRequest):
    return await summarize_text(summarization_request)

@app.post("/generate-quiz", response_model=QuizResponse)
@limiter.limit("5/minute")
async def generate_quiz_endpoint(request: Request, quiz_request: QuizGenerationRequest):
    return await generate_quiz(quiz_request)

@app.post("/extract-key-points", response_model=KeyPointsResponse)
async def extract_key_points_endpoint(request: KeyPointsRequest):
    return await extract_key_points(request)

@app.post("/generate-flashcards", response_model=FlashcardResponse)
async def generate_flashcards_endpoint(request: FlashcardRequest):
    return await generate_flashcards(request)

@app.post("/explain-code", response_model=CodeExplanationResponse)
async def explain_code_endpoint(request: CodeExplanationRequest):
    return await explain_code(request)

@app.post("/process-meeting-notes", response_model=MeetingNotesResponse)
async def process_meeting_notes_endpoint(request: MeetingNotesRequest):
    return await process_meeting_notes(request)

@app.post("/ask-document", response_model=DocumentQAResponse)
async def ask_document_endpoint(request: DocumentQARequest):
    return await ask_document(request)

@app.post("/improve-writing", response_model=WritingImprovementResponse)
async def improve_writing_endpoint(request: WritingImprovementRequest):
    return await improve_writing(request)

@app.post("/moderate-content")
async def moderate_content_endpoint(request: ModerationRequest):
    return await moderate_content(request)

@app.get("/")
async def health_check():
    return {
        "status": "running",
        "services": [
            "summarize", 
            "generate-quiz", 
            "extract-key-points",
            "generate-flashcards",
            "explain-code",
            "process-meeting-notes",
            "ask-document",
            "improve-writing",
            "moderate-content"
        ]
    }

@app.post("/summarize-with-audio")
@limiter.limit("5/minute")
async def summarize_with_audio(request: Request, summarization_request: SummarizationRequest):
    result = await summarize_text(summarization_request)
    audio_url = text_to_speech(result.summary, lang=summarization_request.language[:2])
    return JSONResponse({
        "summary": result.summary,
        "audio_url": audio_url,
        "language": summarization_request.language,
        "original_length": result.original_length,
        "summary_length": result.summary_length
    })

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
# from fastapi import FastAPI, HTTPException, UploadFile, File, WebSocket, Request
# from pydantic import BaseModel
# from typing import List, Dict, Optional
# import os
# import httpx
# import json
# import logging
# from dotenv import load_dotenv
# from slowapi import Limiter
# from slowapi.util import get_remote_address
# from fastapi.middleware import Middleware
# from fastapi.responses import StreamingResponse
# from fastapi.staticfiles import StaticFiles

# # Configure logging
# logging.basicConfig(level=logging.INFO)
# logger = logging.getLogger(__name__)

# # Load environment variables
# load_dotenv()

# # Initialize FastAPI with rate limiting
# limiter = Limiter(key_func=get_remote_address)
# app = FastAPI()
# app.state.limiter = limiter
# app.mount("/static", StaticFiles(directory="static"), name="static")

# # Configuration - with validation
# GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
# if not GITHUB_TOKEN:
#     logger.error("GITHUB_TOKEN environment variable is not set")
#     raise RuntimeError("GITHUB_TOKEN environment variable is required")

# ENDPOINT = os.getenv("API_ENDPOINT", "https://models.github.ai/inference/chat/completions")
# MODEL = os.getenv("MODEL_NAME", "openai/gpt-4.1")

# headers = {
#     "Authorization": f"Bearer {GITHUB_TOKEN}",
#     "Content-Type": "application/json",
#     "Accept": "application/json"
# }

# # ========== MODELS ==========
# class SummarizationRequest(BaseModel):
#     text: str
#     language: str = "english"

# class SummarizationResponse(BaseModel):
#     summary: str
#     language: str
#     original_length: int
#     summary_length: int

# class QuizGenerationRequest(BaseModel):
#     topic: str
#     num_questions: int = 5
#     difficulty: str = "medium"
#     language: str = "english"

# class QuizQuestion(BaseModel):
#     question: str
#     options: List[str]
#     correct_answer: str
#     explanation: Optional[str] = None

# class QuizResponse(BaseModel):
#     quiz: List[QuizQuestion]
#     topic: str
#     difficulty: str
#     language: str

# class KeyPointsRequest(BaseModel):
#     text: str
#     num_points: int = 5

# class KeyPointsResponse(BaseModel):
#     key_points: List[str]
#     original_length: int

# class Flashcard(BaseModel):
#     term: str
#     definition: str
#     context: Optional[str]

# class FlashcardRequest(BaseModel):
#     topic: str
#     num_cards: int = 10

# class FlashcardResponse(BaseModel):
#     flashcards: List[Flashcard]
#     topic: str
#     num_cards: int

# class CodeExplanationRequest(BaseModel):
#     code: str
#     language: str = "python"
#     detail_level: str = "intermediate"

# class CodeExplanationResponse(BaseModel):
#     explanation: str
#     language: str
#     detail_level: str

# class MeetingNotesRequest(BaseModel):
#     transcript: str
#     include_actions: bool = True
#     include_decisions: bool = True

# class MeetingNotesResponse(BaseModel):
#     summary: str
#     action_items: Optional[List[str]] = None
#     key_decisions: Optional[List[str]] = None
#     follow_up_questions: Optional[List[str]] = None

# class DocumentQARequest(BaseModel):
#     question: str
#     context: str

# class DocumentQAResponse(BaseModel):
#     answer: str
#     confidence: Optional[str] = None

# class WritingImprovementRequest(BaseModel):
#     text: str
#     style: str = "professional"

# class WritingImprovementResponse(BaseModel):
#     improved_text: str
#     changes: List[str]
#     original_length: int
#     improved_length: int

# class ModerationRequest(BaseModel):
#     text: str

# # ========== HELPER FUNCTIONS ==========
# async def call_github_inference(messages: List[Dict[str, str]], temperature: float = 0.7) -> str:
#     """Generic function to call GitHub's inference endpoint with robust error handling"""
#     payload = {
#         "messages": messages,
#         "temperature": temperature,
#         "model": MODEL
#     }
    
#     try:
#         async with httpx.AsyncClient(timeout=30.0) as client:
#             logger.info(f"Sending request to {ENDPOINT}")
#             response = await client.post(ENDPOINT, headers=headers, json=payload)
            
#             response.raise_for_status()
#             response_data = response.json()
            
#             if "choices" not in response_data or not response_data["choices"]:
#                 raise ValueError("Invalid response format - missing choices")
            
#             return response_data["choices"][0]["message"]["content"]
            
#     except httpx.HTTPStatusError as e:
#         error_msg = f"API request failed with status {e.response.status_code}: {e.response.text}"
#         logger.error(error_msg)
#         raise HTTPException(status_code=e.response.status_code, detail=error_msg)
#     except json.JSONDecodeError as e:
#         error_msg = f"Failed to decode JSON response: {str(e)}"
#         logger.error(error_msg)
#         raise HTTPException(status_code=500, detail=error_msg)
#     except Exception as e:
#         error_msg = f"Unexpected error: {str(e)}"
#         logger.error(error_msg)
#         raise HTTPException(status_code=500, detail=error_msg)

# # ========== CORE ENDPOINTS ==========
# @app.post("/summarize", response_model=SummarizationResponse)
# @limiter.limit("10/minute")
# async def summarize(request: Request, summarization_request: SummarizationRequest):
#     """Summarize text with rate limiting"""
#     text = summarization_request.text.strip()
#     if not text:
#         raise HTTPException(status_code=400, detail="Text is required.")
    
#     if len(text) > 10000:
#         raise HTTPException(status_code=400, detail="Text too long. Maximum 10,000 characters allowed.")

#     try:
#         messages = [
#             {"role": "system", "content": f"You are a helpful assistant that summarizes text in {summarization_request.language}."},
#             {"role": "user", "content": f"Please summarize the following text in {summarization_request.language}:\n\n{text}"}
#         ]
        
#         summary = await call_github_inference(messages)
#         return SummarizationResponse(
#             summary=summary,
#             language=summarization_request.language,
#             original_length=len(text),
#             summary_length=len(summary))
#     except Exception as e:
#         logger.error(f"Summarization failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to generate summary")

# @app.post("/generate-quiz", response_model=QuizResponse)
# @limiter.limit("5/minute")
# async def generate_quiz(request: Request, quiz_request: QuizGenerationRequest):
#     """Generate quiz questions with rate limiting"""
#     if quiz_request.num_questions < 1 or quiz_request.num_questions > 20:
#         raise HTTPException(status_code=400, detail="Number of questions must be between 1 and 20")

#     try:
#         system_prompt = f"""Generate a {quiz_request.difficulty} quiz about {quiz_request.topic} with {quiz_request.num_questions} questions.
#         For each question provide:
#         - Clear question
#         - 4 options (labeled A-D)
#         - Correct answer (letter only)
#         - Brief explanation
        
#         Return JSON format: {{"questions": [{{"question": "...", "options": ["A...", ...], "correct_answer": "A", "explanation": "..."}}]}}"""
        
#         quiz_json_str = await call_github_inference([
#             {"role": "system", "content": system_prompt},
#             {"role": "user", "content": f"Topic: {quiz_request.topic}"}
#         ], temperature=0.5)
        
#         quiz_data = json.loads(quiz_json_str)
#         return QuizResponse(
#             quiz=[QuizQuestion(**q) for q in quiz_data["questions"]],
#             topic=quiz_request.topic,
#             difficulty=quiz_request.difficulty,
#             language=quiz_request.language
#         )
#     except json.JSONDecodeError:
#         raise HTTPException(status_code=500, detail="Failed to parse quiz response")
#     except Exception as e:
#         logger.error(f"Quiz generation failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to generate quiz")

# # ========== ADDITIONAL ENDPOINTS ==========
# @app.post("/extract-key-points", response_model=KeyPointsResponse)
# async def extract_key_points(request: KeyPointsRequest):
#     """Extract key points from text"""
#     try:
#         prompt = f"""Extract {request.num_points} key points from this text:
#         {request.text}
        
#         Return as JSON array: ["point1", "point2", ...]"""
        
#         key_points = json.loads(await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.3
#         ))
#         return KeyPointsResponse(
#             key_points=key_points,
#             original_length=len(request.text)
#         )
#     except Exception as e:
#         logger.error(f"Key points extraction failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to extract key points")

# @app.post("/generate-flashcards", response_model=FlashcardResponse)
# async def generate_flashcards(request: FlashcardRequest):
#     """Generate flashcards for a given topic"""
#     try:
#         prompt = f"""Create {request.num_cards} flashcards about {request.topic}.
#         Each flashcard should have:
#         - Term/concept name
#         - Concise definition
#         - Optional example/context
        
#         Return as JSON: {{"flashcards": [{{"term": "...", "definition": "...", "context": "..."}}]}}"""
        
#         response = await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.4
#         )
#         flashcards_data = json.loads(response)
#         return FlashcardResponse(
#             flashcards=[Flashcard(**card) for card in flashcards_data["flashcards"]],
#             topic=request.topic,
#             num_cards=request.num_cards
#         )
#     except Exception as e:
#         logger.error(f"Flashcard generation failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to generate flashcards")

# @app.post("/explain-code", response_model=CodeExplanationResponse)
# async def explain_code(request: CodeExplanationRequest):
#     """Explain code at specified detail level"""
#     try:
#         prompt = f"""Explain this {request.language} code ({request.detail_level} level):
#         {request.code}
        
#         Include:
#         1. Purpose
#         2. Key functions/structures
#         3. Inputs/outputs
#         4. {'Complexity analysis' if request.detail_level == 'advanced' else 'Key algorithms' if request.detail_level == 'intermediate' else 'Basic functionality'}"""
        
#         explanation = await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.2
#         )
#         return CodeExplanationResponse(
#             explanation=explanation,
#             language=request.language,
#             detail_level=request.detail_level
#         )
#     except Exception as e:
#         logger.error(f"Code explanation failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to explain code")

# @app.post("/process-meeting-notes", response_model=MeetingNotesResponse)
# async def process_meeting_notes(request: MeetingNotesRequest):
#     """Process meeting transcript into structured notes"""
#     try:
#         components = []
#         if request.include_actions:
#             components.append("action items (who/what/when)")
#         if request.include_decisions:
#             components.append("key decisions")
        
#         prompt = f"""Process meeting transcript:
#         {request.transcript}
        
#         Extract:
#         1. Brief summary
#         2. {', '.join(components)}
#         3. Follow-up questions
        
#         Return as JSON with these fields: summary, action_items, key_decisions, follow_up_questions"""
        
#         notes_data = json.loads(await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.3
#         ))
#         return MeetingNotesResponse(**notes_data)
#     except Exception as e:
#         logger.error(f"Meeting notes processing failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to process meeting notes")

# @app.post("/ask-document", response_model=DocumentQAResponse)
# async def ask_document(request: DocumentQARequest):
#     """Answer questions based on document context"""
#     try:
#         prompt = f"""Answer based on context:
#         Question: {request.question}
#         Context: {request.context}
        
#         If unanswerable, respond: "I cannot determine from the given context".
#         Return as JSON: {{"answer": "...", "confidence": "high/medium/low"}}"""
        
#         response = await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.1
#         )
#         return DocumentQAResponse(**json.loads(response))
#     except Exception as e:
#         logger.error(f"Document QA failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to answer question")

# @app.post("/improve-writing", response_model=WritingImprovementResponse)
# async def improve_writing(request: WritingImprovementRequest):
#     """Improve writing style"""
#     try:
#         prompt = f"""Rewrite in {request.style} style:
#         Original: {request.text}
        
#         Provide:
#         1. Improved version
#         2. List of changes
        
#         Return as JSON: {{"improved_text": "...", "changes": ["...", ...], "original_length": N, "improved_length": N}}"""
        
#         response = await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.5
#         )
#         return WritingImprovementResponse(**json.loads(response))
#     except Exception as e:
#         logger.error(f"Writing improvement failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to improve writing")

# @app.post("/moderate-content")
# async def moderate_content(request: ModerationRequest):
#     """Analyze text for harmful content"""
#     try:
#         prompt = f"""Analyze for harmful content:
#         {request.text}
        
#         Return JSON with:
#         - is_flagged (boolean)
#         - categories (object with scores)
#         - reasons (array)"""
        
#         return json.loads(await call_github_inference(
#             [{"role": "user", "content": prompt}],
#             temperature=0.1
#         ))
#     except Exception as e:
#         logger.error(f"Content moderation failed: {str(e)}")
#         raise HTTPException(status_code=500, detail="Failed to moderate content")

# # ========== HEALTH CHECK ==========
# @app.get("/")
# async def health_check():
#     """Basic health check endpoint"""
#     return {
#         "status": "running",
#         "services": [
#             "summarize", 
#             "generate-quiz", 
#             "extract-key-points",
#             "generate-flashcards",
#             "explain-code",
#             "process-meeting-notes",
#             "ask-document",
#             "improve-writing",
#             "moderate-content"
#         ]
#     }

# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run(app, host="0.0.0.0", port=8000)