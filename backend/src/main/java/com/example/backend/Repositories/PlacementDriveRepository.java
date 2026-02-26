package com.example.backend.Repositories;

import com.example.backend.Models.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    java.util.List<PlacementDrive> findByStatusIn(
            java.util.List<com.example.backend.Models.enums.DriveStatus> statuses);

    long countByStatus(com.example.backend.Models.enums.DriveStatus status);

    java.util.Optional<PlacementDrive> findTopByOrderByCtcLpaDesc();
}
