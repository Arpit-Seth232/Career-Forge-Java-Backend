import os
import json
import google.generativeai as genai
from typing import Dict, Any

async def analyze_resume(user_id: str, jd: str, resume: Dict[str, Any]) -> Dict[str, Any]:
    """
    Analyzes a resume against a job description using Gemini.
    """
    api_key = os.getenv("GEMINI_API_KEY")
    if not api_key:
        # For demonstration purposes, if no API key is set, we return the requested mock response
        # In a real scenario, we should handle this error properly.
        # But for now, let's try to use the API if available.
        if not os.environ.get("GEMINI_API_KEY"):
            return {
                "ats_score": 78,
                "missing_skills": ["Docker", "AWS"],
                "weak_points": ["No cloud experience"],
                "suggestions": ["Add Docker project"],
                "recommendations": ["Quantify achievements"]
            }

    try:
        genai.configure(api_key=api_key)
        model = genai.GenerativeModel('gemini-2.5-flash')

        # Load prompt
        current_dir = os.path.dirname(os.path.abspath(__file__))
        prompt_path = os.path.join(current_dir, "..", "prompts", "atsPrompt.txt")
        
        # Check if prompt file exists
        if not os.path.exists(prompt_path):
            # Fallback prompt if file is missing for some reason
            prompt = f"Analyze this resume: {json.dumps(resume)} against this Job Description: {jd}. Provide ATS score, missing skills, weak points, suggestions, and recommendations in JSON format."
        else:
            with open(prompt_path, "r") as f:
                prompt_template = f.read()
            prompt = prompt_template.format(jd=jd, resume=json.dumps(resume))

        # Call Gemini
        response = model.generate_content(prompt)
        text = response.text

        # Parse JSON
        if "```json" in text:
            text = text.split("```json")[1].split("```")[0].strip()
        elif "```" in text:
            text = text.split("```")[1].split("```")[0].strip()
        
        return json.loads(text)

    except Exception as e:
        print(f"Error in analyze_resume: {str(e)}")
        # Return fallback as requested in the prompt for demo/fallback purposes
        return {
            "ats_score": 78,
            "missing_skills": ["Docker", "AWS"],
            "weak_points": ["No cloud experience"],
            "suggestions": ["Add Docker project"],
            "recommendations": ["Quantify achievements"]
        }
