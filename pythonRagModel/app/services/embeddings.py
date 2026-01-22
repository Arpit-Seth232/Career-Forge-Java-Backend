from google import genai
import os

client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

EMBED_MODEL = "text-embedding-004"

def get_embedding(text: str) -> list[float]:
    response = client.models.embed_content(
        model=EMBED_MODEL,
        contents=text
    )
    return response.embeddings[0].values
