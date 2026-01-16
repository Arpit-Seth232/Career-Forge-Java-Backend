package com.example.Carrer_backend.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Carrer_backend.Entity.mentor;

@Repository
public interface mentorRepository extends JpaRepository<mentor, String> {
    Optional<mentor> findById(String id);

    @Query("SELECT DISTINCT m From mentor m JOIN m.expertise e WHERE LOWER(e) IN :skills")
    List<mentor> findByExpertise(@Param("skills") Set<String> skills);

    @Query("SELECT COUNT(DISTINCT m) FROM mentor m JOIN m.expertise e WHERE LOWER(e) IN :skills")
    Long countByExpertise(@Param("skills") Set<String> skills);

}
