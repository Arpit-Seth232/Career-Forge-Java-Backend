package com.example.Carrer_backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.apiLogs;

@Repository
public interface apiLogRepository extends JpaRepository<apiLogs, Long> {
    
}
