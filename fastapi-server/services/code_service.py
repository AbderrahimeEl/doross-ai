from fastapi import HTTPException
import logging
from models import CodeExplanationRequest, CodeExplanationResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def explain_code(request: CodeExplanationRequest) -> CodeExplanationResponse:
    """Explain code at specified detail level"""
    try:
        prompt = f"""Explain this {request.language} code ({request.detail_level} level):
        {request.code}
        
        Include:
        1. Purpose
        2. Key functions/structures
        3. Inputs/outputs
        4. {'Complexity analysis' if request.detail_level == 'advanced' else 'Key algorithms' if request.detail_level == 'intermediate' else 'Basic functionality'}"""
        
        explanation = await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.2
        )
        return CodeExplanationResponse(
            explanation=explanation,
            language=request.language,
            detail_level=request.detail_level
        )
    except Exception as e:
        logger.error(f"Code explanation failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to explain code")