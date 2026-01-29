import os
import sys
from dotenv import load_dotenv

# Add the project root to sys.path to import app modules
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

load_dotenv()

from app.services.vectorStore import save_jd_embedding, get_jd_embedding, get_jd_hash
from app.services.atsAnalyzer import extract_jd_details
from app.services.embeddings import get_embedding

def test_jd_storage():
    sample_jd = "We are looking for a Senior Python Developer with 5+ years of experience in Django and PostgreSQL. Location: Remote. Salary: $120k-$150k."
    jd_hash = get_jd_hash(sample_jd)
    
    print(f"Testing with JD hash: {jd_hash}")
    
    # 1. Test Extraction
    print("Testing Gemini extraction...")
    jd_content = extract_jd_details(sample_jd)
    print(f"Extracted JD Content: {jd_content}")
    
    if not jd_content:
        print("FAILED: Gemini extraction returned None")
        return

    # 2. Test Saving
    print("Testing saving to database...")
    embedding = get_embedding(sample_jd)
    # Delete old entry if exists for clean test
    from app.db.supabase import get_connection
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM jd_embeddings WHERE jd_hash = %s", (jd_hash,))
    conn.commit()
    
    save_jd_embedding(jd_hash, embedding, jd_content)
    print("Successfully saved to database.")

    # 3. Test Retrieval
    print("Testing retrieval from database...")
    retrieved_embedding, retrieved_content = get_jd_embedding(jd_hash)
    
    print(f"Retrieved Content Type: {type(retrieved_content)}")
    print(f"Retrieved Content: {retrieved_content}")

    if retrieved_content == jd_content:
        print("SUCCESS: Retrieved content matches original.")
    else:
        # JSONB might return as dict automatically via psycopg2 or as string depending on config
        # Let's check both
        import json
        if isinstance(retrieved_content, str):
             if json.loads(retrieved_content) == jd_content:
                 print("SUCCESS: Retrieved content (parsed string) matches original.")
             else:
                 print("FAILED: Retrieved content mismatch.")
        elif isinstance(retrieved_content, dict):
            if retrieved_content == jd_content:
                print("SUCCESS: Retrieved content (dict) matches original.")
            else:
                print("FAILED: Retrieved content mismatch.")
        else:
             print(f"FAILED: Unexpected content type {type(retrieved_content)}")

if __name__ == "__main__":
    test_jd_storage()
