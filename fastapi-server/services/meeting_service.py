from fastapi import HTTPException
import json
import logging
from models import MeetingNotesRequest, MeetingNotesResponse
from .ai_service import call_github_inference

logger = logging.getLogger(__name__)

async def process_meeting_notes(request: MeetingNotesRequest) -> MeetingNotesResponse:
    """Process meeting transcript into structured notes"""
    try:
        components = []
        if request.include_actions:
            components.append("action items (who/what/when)")
        if request.include_decisions:
            components.append("key decisions")
        
        prompt = f"""Process meeting transcript:
        {request.transcript}
        
        Extract:
        1. Brief summary
        2. {', '.join(components)}
        3. Follow-up questions
        
        Return as JSON with these fields: summary, action_items, key_decisions, follow_up_questions"""
        
        notes_data = json.loads(await call_github_inference(
            [{"role": "user", "content": prompt}],
            temperature=0.3
        ))
        return MeetingNotesResponse(**notes_data)
    except Exception as e:
        logger.error(f"Meeting notes processing failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to process meeting notes")