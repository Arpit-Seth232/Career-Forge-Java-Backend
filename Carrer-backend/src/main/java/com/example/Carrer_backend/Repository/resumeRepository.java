package com.example.Carrer_backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.resumeEmbeddedId;

@Repository
public interface resumeRepository extends JpaRepository<resume, resumeEmbeddedId> {
    Optional<resume> findById(resumeEmbeddedId id);

    @Query("SELECT r FROM resume r WHERE r.id.userId = :userId")
    List<resume> findByUserId(String userId);
}
