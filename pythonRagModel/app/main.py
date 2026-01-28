from dotenv import load_dotenv
load_dotenv()

from fastapi import FastAPI
from app.routes.rag import router as rag_router
from app.routes.search import router as search_router
import os

app = FastAPI(title="Career Forge RAG Model API")

# Include routers
app.include_router(rag_router, prefix="/rag", tags=["RAG"])
app.include_router(search_router, prefix="/search", tags=["Search"])

@app.get("/")
async def root():
    return {"message": "Welcome to Career Forge RAG Model API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=9090)
