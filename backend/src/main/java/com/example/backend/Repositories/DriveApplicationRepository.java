package com.example.backend.Repositories;

import com.example.backend.Models.DriveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriveApplicationRepository extends JpaRepository<DriveApplication, Long> {
    boolean existsByStudentProfileIdAndDriveId(Long studentId, Long driveId);

    java.util.List<DriveApplication> findByStudentProfileId(Long studentId);
}
