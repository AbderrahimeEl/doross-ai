from gtts import gTTS
import os
import uuid

AUDIO_OUTPUT_DIR = "static/audio"
os.makedirs(AUDIO_OUTPUT_DIR, exist_ok=True)

def text_to_speech(text: str, lang: str = "en") -> str:
    tts = gTTS(text=text, lang=lang)
    filename = f"{uuid.uuid4().hex}.mp3"
    filepath = os.path.join(AUDIO_OUTPUT_DIR, filename)
    tts.save(filepath)
    return f"/static/audio/{filename}"
