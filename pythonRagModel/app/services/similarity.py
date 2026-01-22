from app.db.supabase import get_connection

def cosine_similarity(query_embedding: list, content_type: str):
    conn = get_connection()
    cur = conn.cursor()

    vector_str = "[" + ",".join(map(str, query_embedding)) + "]"

    cur.execute("""
        SELECT
            1 - (embedding <=> %s::vector) AS similarity
        FROM resume_embeddings
        WHERE content_type = %s
        ORDER BY embedding <=> %s::vector
        LIMIT 1
    """, (vector_str, content_type, vector_str))

    row = cur.fetchone()

    cur.close()
    conn.close()

    return float(row[0]) if row else 0.0
