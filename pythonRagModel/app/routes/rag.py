import os

from app.services.atsAnalyzer import  analyze_resume
from google import genai
from fastapi import APIRouter, Request

router = APIRouter()

client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

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
