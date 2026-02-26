package com.example.backend.Repositories;

import com.example.backend.Models.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
}
