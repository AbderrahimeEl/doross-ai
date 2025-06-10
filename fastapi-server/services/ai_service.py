import httpx
import json
import logging
from fastapi import HTTPException
from config import ENDPOINT, headers, MODEL

logger = logging.getLogger(__name__)

async def call_github_inference(messages: list[dict[str, str]], temperature: float = 0.7) -> str:
    """Generic function to call GitHub's inference endpoint with robust error handling"""
    payload = {
        "messages": messages,
        "temperature": temperature,
        "model": MODEL
    }
    
    try:
        async with httpx.AsyncClient(timeout=30.0) as client:
            logger.info(f"Sending request to {ENDPOINT}")
            response = await client.post(ENDPOINT, headers=headers, json=payload)
            
            response.raise_for_status()
            response_data = response.json()
            
            if "choices" not in response_data or not response_data["choices"]:
                raise ValueError("Invalid response format - missing choices")
            
            return response_data["choices"][0]["message"]["content"]
            
    except httpx.HTTPStatusError as e:
        error_msg = f"API request failed with status {e.response.status_code}: {e.response.text}"
        logger.error(error_msg)
        raise HTTPException(status_code=e.response.status_code, detail=error_msg)
    except json.JSONDecodeError as e:
        error_msg = f"Failed to decode JSON response: {str(e)}"
        logger.error(error_msg)
        raise HTTPException(status_code=500, detail=error_msg)
    except Exception as e:
        error_msg = f"Unexpected error: {str(e)}"
        logger.error(error_msg)
        raise HTTPException(status_code=500, detail=error_msg)