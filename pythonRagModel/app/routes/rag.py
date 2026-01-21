from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.services.atsAnalyzer import analyze_resume
import base64
import json
import io
try:
    import fitz  # PyMuPDF
except ImportError:
    fitz = None

router = APIRouter()

class AnalysisRequest(BaseModel):
    userId: str
    jd: str  # Base64 encoded
    resumeContent: dict  # JSON node

@router.post("/analyze")
async def analyze(request: AnalysisRequest):
    try:
        # Decode Job Description
        jd_bytes = base64.b64decode(request.jd)
    except Exception as e:
        print(f"Base64 decode error: {str(e)}")
        raise HTTPException(status_code=400, detail=f"Invalid Base64 encoding: {str(e)}")

    decoded_jd = ""
    try:
        decoded_jd = jd_bytes.decode('utf-8')
    except Exception as e:
        print(f"UTF-8 decode error, trying PDF extraction: {str(e)}")
        if fitz:
            try:
                pdf_stream = io.BytesIO(jd_bytes)
                doc = fitz.open(stream=pdf_stream, filetype="pdf")
                text = ""
                for page in doc:
                    text += page.get_text()
                doc.close()
                decoded_jd = text.strip()
                if not decoded_jd:
                    raise Exception("PDF is empty or has no extractable text")
            except Exception as pdf_error:
                print(f"PDF extraction error: {str(pdf_error)}")
                raise HTTPException(status_code=400, detail=f"Failed to extract text from JD (not UTF-8 and PDF parsing failed): {str(pdf_error)}")
        else:
            raise HTTPException(status_code=400, detail="Invalid UTF-8 content in JD and PDF support not installed.")

    try:
        # Analyze resume using the service
        result = await analyze_resume(request.userId, decoded_jd, request.resumeContent)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
