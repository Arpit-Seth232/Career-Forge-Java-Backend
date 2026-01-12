package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.recruiter;

@Repository
public interface recruiterRepository extends JpaRepository<recruiter, String>{
    Optional<recruiter> findById(String id);
}
