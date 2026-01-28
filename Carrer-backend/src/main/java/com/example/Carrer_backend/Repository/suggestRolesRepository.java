package com.example.Carrer_backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.suggestRole;

@Repository
public interface suggestRolesRepository extends JpaRepository<suggestRole, Integer> {

    @Query(value = "SELECT * FROM suggested_roles sr WHERE sr.user_id = :userId AND sr.file_name = :fileName", nativeQuery = true)
    Optional<suggestRole> findByUserIdAndFileName(String userId, String fileName);

}
