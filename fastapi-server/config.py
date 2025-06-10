import os
import logging
from dotenv import load_dotenv

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load environment variables
load_dotenv()

# Configuration - with validation
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
if not GITHUB_TOKEN:
    logger.error("GITHUB_TOKEN environment variable is not set")
    raise RuntimeError("GITHUB_TOKEN environment variable is required")

ENDPOINT = os.getenv("API_ENDPOINT", "https://models.github.ai/inference/chat/completions")
MODEL = os.getenv("MODEL_NAME", "openai/gpt-4.1")

headers = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Content-Type": "application/json",
    "Accept": "application/json"
}