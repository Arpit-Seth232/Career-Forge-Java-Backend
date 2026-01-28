package com.example.Carrer_backend.Repository;

import java.util.ArrayList;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.resumeEmbed;

@Repository
public interface resumeEmbedRepository extends JpaRepository<resumeEmbed, Integer> {

    @Query(value = "SELECT e.embedding FROM resume_embeddings e WHERE e.user_id = :userId", nativeQuery = true)
    ArrayList<?> findByUserId(String userId);
}
