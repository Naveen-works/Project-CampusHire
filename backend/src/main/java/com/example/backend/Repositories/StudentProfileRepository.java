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

    long countByVerificationStatus(com.example.backend.Models.enums.VerificationStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(MAX(s.highestPackageLpa), 0) FROM StudentProfile s")
    Double findHighestCtc();

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(AVG(s.highestPackageLpa), 0) FROM StudentProfile s WHERE s.isPlaced = true")
    Double findAverageCtc();

    @org.springframework.data.jpa.repository.Query("SELECT s FROM StudentProfile s WHERE s.verificationStatus = 'VERIFIED' AND (:query IS NULL OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.rollNo) LIKE LOWER(CONCAT('%', :query, '%')) )")
    java.util.List<StudentProfile> searchVerifiedStudents(
            @org.springframework.data.repository.query.Param("query") String query);

    @org.springframework.data.jpa.repository.Query("SELECT d.name, COUNT(s) FROM StudentProfile s JOIN s.user u JOIN u.department d WHERE s.isPlaced = true GROUP BY d.name")
    java.util.List<Object[]> countPlacementsByDepartment();

    // Faculty Scoped Queries
    java.util.List<StudentProfile> findByUserDepartmentId(Long departmentId);

    long countByUserDepartmentId(Long departmentId);

    long countByUserDepartmentIdAndVerificationStatus(Long departmentId,
            com.example.backend.Models.enums.VerificationStatus status);

    long countByUserDepartmentIdAndIsPlacedTrue(Long departmentId);
}
