package com.example.backend.Repositories;

import com.example.backend.Models.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByRollNo(String rollNo);

    Optional<StudentProfile> findByUserEmail(String email);

    long countByIsPlacedTrue();
}
