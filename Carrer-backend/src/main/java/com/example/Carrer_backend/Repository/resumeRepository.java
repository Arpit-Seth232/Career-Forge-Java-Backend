package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.resume;
import com.example.Carrer_backend.Entity.resumeEmbeddedId;

@Repository
public interface resumeRepository extends JpaRepository<resume, resumeEmbeddedId> {
    Optional<resume> findById(resumeEmbeddedId id);
    
}
