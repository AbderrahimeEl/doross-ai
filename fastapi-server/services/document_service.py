from fastapi import HTTPException
import json
import logging
from models import DocumentQARequest, DocumentQAResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def ask_document(request: DocumentQARequest) -> DocumentQAResponse:
    """Answer questions based on document context"""
    try:
        prompt = f"""Answer based on context:
        Question: {request.question}
        Context: {request.context}
        
        If unanswerable, respond: "I cannot determine from the given context".
        Return as JSON: {{"answer": "...", "confidence": "high/medium/low"}}"""
        
        response = await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.1
        )
        return DocumentQAResponse(**json.loads(response))
    except Exception as e:
        logger.error(f"Document QA failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to answer question")