package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.user;

@Repository
public interface userRepository extends JpaRepository<user, String> {
    public boolean existsByEmail(String email);
    public Optional<user> findByEmail(String email);
}
