from fastapi import HTTPException
import json
import logging
from models import WritingImprovementRequest, WritingImprovementResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def improve_writing(request: WritingImprovementRequest) -> WritingImprovementResponse:
    """Improve writing style"""
    try:
        prompt = f"""Rewrite in {request.style} style:
        Original: {request.text}
        
        Provide:
        1. Improved version
        2. List of changes
        
        Return as JSON: {{"improved_text": "...", "changes": ["...", ...], "original_length": N, "improved_length": N}}"""
        
        response = await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.5
        )
        return WritingImprovementResponse(**json.loads(response))
    except Exception as e:
        logger.error(f"Writing improvement failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to improve writing")