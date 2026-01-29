import json
import os
import base64
import io
import fitz  # PyMuPDF
from google import genai
from app.services.embeddings import get_embedding
from app.services.vectorStore import (
    get_resume_embedding,
    save_resume_embedding,
    get_jd_embedding,
    save_jd_embedding,
    get_jd_hash
)

client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

def extract_json_from_text(text: str) -> str:
    """Extracts JSON content between the first { and last }."""
    start = text.find('{')
    end = text.rfind('}')
    if start != -1 and end != -1 and end > start:
        return text[start:end+1]
    return text

def extract_text_from_base64_pdf(base64_str: str) -> str:
    """Decodes base64 and extracts text from PDF using PyMuPDF."""
    try:
        pdf_data = base64.b64decode(base64_str)
        pdf_stream = io.BytesIO(pdf_data)
        doc = fitz.open(stream=pdf_stream, filetype="pdf")
        text = ""
        for page in doc:
            text += page.get_text()
        doc.close()
        return text.strip()
    except Exception as e:
        print(f"Error extracting text from JD PDF: {e}")
        return base64_str # Fallback to original

SIMILARITY_THRESHOLD = 0.8


def cosine_similarity(v1, v2):
    dot = sum(a * b for a, b in zip(v1, v2))
    mag1 = sum(a * a for a in v1) ** 0.5
    mag2 = sum(b * b for b in v2) ** 0.5
    return dot / (mag1 * mag2)

def extract_jd_details(jd_text: str):
    """Passes JD text to Gemini to get JD details in JSON format."""

    prompt_path = os.path.join(os.path.dirname(__file__), '..', 'prompts', 'jdDetailsExtractionPrompt.txt')
    with open(prompt_path, 'r', encoding='utf-8') as f:
        prompt_template = f.read()
        
    prompt = prompt_template.format(jd_text=jd_text)

    try:
        response = client.models.generate_content(
            model="gemini-2.5-flash",
            contents=prompt
        )
        text = response.text.strip()
        json_str = extract_json_from_text(text)
        return json.loads(json_str)
    except Exception as e:
        print(f"Error extracting JD details with Gemini: {e}")
        return None


async def analyze_resume(user_id: str, jd: str, resume_json: dict):
    # Check if JD is Base64 encoded PDF
    if jd.startswith("JVBERi"):
        print("DEBUG: Detected Base64 PDF Job Description in atsAnalyzer. Extracting text...")
        jd = extract_text_from_base64_pdf(jd)

    resume_text = json.dumps(resume_json)
    file_name = resume_json.get("fileName", "resume.pdf")

    # ---------- Resume Embedding ----------
    resume_embedding = get_resume_embedding(user_id, file_name)
    if not resume_embedding:
        resume_embedding = get_embedding(resume_text)
        save_resume_embedding(user_id, file_name, resume_embedding)

    # ---------- JD Embedding ----------
    jd_hash = get_jd_hash(jd)
    jd_embedding = get_jd_embedding(jd_hash)
    if not jd_embedding:
        jd_embedding = get_embedding(jd)
        save_jd_embedding(jd_hash, jd_embedding)

    # ---------- Similarity ----------
    similarity = cosine_similarity(resume_embedding, jd_embedding)

    if similarity >= SIMILARITY_THRESHOLD:
        return {
            "ats_score": int(similarity * 100),
            "similarity": similarity,
            "message": "Resume already matches JD strongly (Gemini skipped)"
        }

    # ---------- Gemini ----------
    prompt_path = os.path.join(os.path.dirname(__file__), '..', 'prompts', 'atsPrompt.txt')
    with open(prompt_path, 'r', encoding='utf-8') as f:
        prompt_template = f.read()
        
    # Format prompt with actual JD and resume
    prompt = prompt_template.format(jd=jd, resume=resume_text)

    response = client.models.generate_content(
        model="gemini-2.5-flash",
        contents=prompt
    )
    
    text = response.text.strip()
    json_str = extract_json_from_text(text)
    
    try:
        return json.loads(json_str)
    except json.JSONDecodeError as je:
        print(f"Failed to parse JSON in atsAnalyzer. Raw text: {text}")
        raise je
