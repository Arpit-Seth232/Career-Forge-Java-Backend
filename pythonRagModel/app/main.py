from fastapi import FastAPI
from app.routes.rag import router as rag_router
from dotenv import load_dotenv
import os

load_dotenv()

app = FastAPI(title="Career Forge RAG Model API")

# Include routers
app.include_router(rag_router, prefix="/rag", tags=["RAG"])

@app.get("/")
async def root():
    return {"message": "Welcome to Career Forge RAG Model API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=9090)
