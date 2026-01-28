from app.services.embeddings import get_embedding
from app.services.vectorStore import (
    get_resume_embedding,
    save_resume_embedding,
    get_jd_embedding,
    save_jd_embedding,
    get_jd_hash
)
import json
import os
import base64
import io
import fitz  # PyMuPDF
from google import genai
from fastapi import APIRouter, Request

router = APIRouter()

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

def extract_jd_details(jd_text: str):
    """Passes JD text to Gemini to get JD details in JSON format."""
    prompt = f"""
    Extract all details from this Job Description and return it in a structured JSON format.
    Include fields like job_title, company, location, requirements, responsibilities, skills, experience_required, etc.
    
    Job Description:
    {jd_text}
    
    Return ONLY a valid JSON object.
    """
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

SIMILARITY_THRESHOLD = 0.75


@router.post("/analyze")
async def analyze(request: Request):
    try:
        data = await request.json()
        user_id = data.get("userId")
        jd = data.get("jd")
        resume_json = data.get("resumeContent")
        file_name = data.get("fileName", "resume.pdf")

        if not all([user_id, jd, resume_json]):
            return {"error": "userId, jd, and resumeContent are required"}

        result = await analyze_resume(user_id, jd, resume_json, file_name)
        return result
    except Exception as e:
        print(f"Error in /analyze: {str(e)}")
        return {"error": str(e)}

async def analyze_resume(user_id, jd, resume_json,file_name):
    try:
        # Check if JD is Base64 encoded PDF
        if jd.startswith("JVBERi"):
            print("DEBUG: Detected Base64 PDF Job Description. Extracting text...")
            jd = extract_text_from_base64_pdf(jd)
            if not jd:
                return {"error": "Could not extract text from Job Description PDF"}

        resume_text = json.dumps(resume_json)

        # -------- Resume embedding --------
        resume_embedding = get_resume_embedding(user_id, file_name)
        if not resume_embedding:
            resume_embedding = get_embedding(resume_text)
            save_resume_embedding(user_id, file_name, resume_embedding)

        # -------- JD embedding --------
        jd_hash = get_jd_hash(jd)
        jd_embedding, jd_content = get_jd_embedding(jd_hash)
        if not jd_embedding:
            jd_embedding = get_embedding(jd)
            jd_content = extract_jd_details(jd)
            save_jd_embedding(jd_hash, jd_embedding, jd_content)

        # -------- Similarity --------
        similarity = sum(
            r * j for r, j in zip(resume_embedding, jd_embedding)
        ) / (
            (sum(r*r for r in resume_embedding) ** 0.5) *
            (sum(j*j for j in jd_embedding) ** 0.5)
        )

        # -------- Decision --------
        if similarity >= SIMILARITY_THRESHOLD:
            return {
                "ats_score": int(similarity * 100),
                "message": "High similarity – Gemini skipped",
                "similarity": similarity
            }

        # -------- Gemini call --------
        prompt = f"""
        Analyze this resume against the job description.

        Resume:
        {resume_text}

        Job Description:
        {jd}

        Return ONLY a JSON object with these keys:
        ats_score (integer), missing_skills (list), weak_points (list), suggestions (list), recommendations (list)
        """

        response = client.models.generate_content(
            model="gemini-2.5-flash",
            contents=prompt
        )

        text = response.text.strip()
        json_str = extract_json_from_text(text)
        
        try:
            return json.loads(json_str)
        except json.JSONDecodeError as je:
            print(f"Failed to parse JSON. Raw text: {text}")
            raise je
    except Exception as e:
        print(f"Error in analyze_resume: {str(e)}")
        raise e
