from app.db.supabase import get_connection

def search_similar_jds(embedding):
    conn = get_connection()
    cursor = conn.cursor()
    # 1 - (embedding <=> %s) gives cosine similarity
    # We cast the input list to a string format that pgvector understands: '[v1, v2, ...]'
    embedding_str = "[" + ",".join(map(str, embedding)) + "]"
    
    query = """
        SELECT jd_content, 1 - (embedding <=> %s::vector) AS matching_score
        FROM jd_embeddings
        ORDER BY matching_score DESC
    """
    cursor.execute(query, (embedding_str,))
    results = cursor.fetchall()
    cursor.close()
    conn.close()
    
    return [{"matching_score": int(r[1] * 100), "data": r[0]} for r in results]
