from app.db.supabase import get_connection
import hashlib

# ---------------- Resume Embeddings ----------------

def get_resume_embedding(user_id: str, file_name: str):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute(
        "SELECT embedding FROM resume_embeddings WHERE user_id = %s AND file_name = %s",
        (user_id, file_name)
    )
    result = cursor.fetchone()
    cursor.close()
    conn.close()
    
    val = result[0] if result else None
    if val is None:
        return None
    if isinstance(val, str):
        val = val.strip("[]{}").split(",")
    return [float(x) for x in val]


def save_resume_embedding(user_id: str, file_name: str, embedding):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO resume_embeddings (user_id, file_name, embedding) VALUES (%s, %s, %s)",
        (user_id, file_name, embedding)
    )
    conn.commit()
    cursor.close()
    conn.close()


# ---------------- JD Embeddings ----------------

def get_jd_hash(jd: str) -> str:
    return hashlib.sha256(jd.encode()).hexdigest()


def get_jd_embedding(jd_hash: str):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute(
        "SELECT embedding, jd_content FROM jd_embeddings WHERE jd_hash = %s",
        (jd_hash,)
    )
    result = cursor.fetchone()
    cursor.close()
    conn.close()
    
    if not result:
        return None, None
    
    val, jd_content = result
    if val is None:
        return None, jd_content
    
    if isinstance(val, str):
        val = val.strip("[]{}").split(",")
    return [float(x) for x in val], jd_content


def save_jd_embedding(jd_hash: str, embedding, jd_content):
    conn = get_connection()
    cursor = conn.cursor()
    import json
    cursor.execute(
        "INSERT INTO jd_embeddings (jd_hash, embedding, jd_content) VALUES (%s, %s, %s)",
        (jd_hash, embedding, json.dumps(jd_content) if jd_content else None)
    )
    conn.commit()
    cursor.close()
    conn.close()
