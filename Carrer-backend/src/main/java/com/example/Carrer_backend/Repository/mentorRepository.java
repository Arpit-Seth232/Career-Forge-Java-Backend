package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.mentor;

@Repository
public interface mentorRepository extends JpaRepository<mentor, String> {
    Optional<mentor> findById(String id);
    
}
