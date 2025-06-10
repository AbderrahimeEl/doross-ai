from fastapi import HTTPException
import json
import logging
from models import KeyPointsRequest, KeyPointsResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def extract_key_points(request: KeyPointsRequest) -> KeyPointsResponse:
    """Extract key points from text"""
    try:
        prompt = f"""Extract {request.num_points} key points from this text:
        {request.text}
        
        Return as JSON array: ["point1", "point2", ...]"""
        
        key_points = json.loads(await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.3
        ))
        return KeyPointsResponse(
            key_points=key_points,
            original_length=len(request.text)
        )
    except Exception as e:
        logger.error(f"Key points extraction failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to extract key points")