package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.jobseeker;

@Repository
public interface jobseekerRepository extends JpaRepository<jobseeker, String> {
    Optional<jobseeker> findById(String id);
}
