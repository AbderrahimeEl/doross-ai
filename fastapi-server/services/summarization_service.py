from fastapi import HTTPException
import logging
from models import SummarizationRequest, SummarizationResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def summarize_text(summarization_request: SummarizationRequest) -> SummarizationResponse:
    """Summarize text with rate limiting"""
    text = summarization_request.text.strip()
    if not text:
        raise HTTPException(status_code=400, detail="Text is required.")
    
    if len(text) > 10000:
        raise HTTPException(status_code=400, detail="Text too long. Maximum 10,000 characters allowed.")

    try:
        messages = [
            {"role": "system", "content": f"You are a helpful assistant that summarizes text in {summarization_request.language}."},
            {"role": "user", "content": f"Please summarize the following text in {summarization_request.language}:\n\n{text}"}
        ]
        
        summary = await call_github_inference(messages)
        return SummarizationResponse(
            summary=summary,
            language=summarization_request.language,
            original_length=len(text),
            summary_length=len(summary))
    except Exception as e:
        logger.error(f"Summarization failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to generate summary")