from fastapi import APIRouter, Request
from app.services.search_service import search_similar_jds

router = APIRouter()

@router.post("/similarity-search")
async def similarity_search_endpoint(request: Request):
    try:
        data = await request.json()
        user_id = data.get("userId")
        resume_embedding = data.get("resumeEmbedding")

        if not user_id or resume_embedding is None:
            return {"error": "userId and resumeEmbedding are required"}

        if not isinstance(resume_embedding, list):
            return {"error": "resumeEmbedding must be a list of floats"}

        results = search_similar_jds(resume_embedding)
        return results
    except Exception as e:
        print(f"Error in /similarity-search: {str(e)}")
        return {"error": str(e)}
