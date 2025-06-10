from fastapi import HTTPException
import json
import logging
from models import ModerationRequest
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def moderate_content(request: ModerationRequest):
    """Analyze text for harmful content"""
    try:
        prompt = f"""Analyze for harmful content:
        {request.text}
        
        Return JSON with:
        - is_flagged (boolean)
        - categories (object with scores)
        - reasons (array)"""
        
        return json.loads(await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.1
        ))
    except Exception as e:
        logger.error(f"Content moderation failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to moderate content")