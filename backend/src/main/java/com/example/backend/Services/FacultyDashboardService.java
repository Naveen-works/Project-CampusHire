package com.example.backend.Services;

import com.example.backend.DTOs.Faculty.FacultyDashboardStatsDTO;
import com.example.backend.Exceptions.ResourceNotFoundException;
import com.example.backend.Exceptions.UnauthorizedActionException;
import com.example.backend.Models.User;
import com.example.backend.Models.enums.DriveStatus;
import com.example.backend.Models.enums.VerificationStatus;
import com.example.backend.Repositories.DriveApplicationRepository;
import com.example.backend.Repositories.PlacementDriveRepository;
import com.example.backend.Repositories.StudentProfileRepository;
import com.example.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacultyDashboardService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private PlacementDriveRepository placementDriveRepository;

    @Autowired
    private UserRepository userRepository;

    public FacultyDashboardStatsDTO getDepartmentStats(String facultyEmail) {
        User faculty = userRepository.findByEmail(facultyEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));

        if (faculty.getDepartment() == null) {
            throw new UnauthorizedActionException("Faculty is not assigned to any department");
        }

        Long departmentId = faculty.getDepartment().getId();

        long totalStudents = studentProfileRepository.countByUserDepartmentId(departmentId);
        long verifiedStudents = studentProfileRepository.countByUserDepartmentIdAndVerificationStatus(departmentId,
                VerificationStatus.VERIFIED);
        long pendingVerifications = studentProfileRepository.countByUserDepartmentIdAndVerificationStatus(departmentId,
                VerificationStatus.PENDING);
        long placedStudents = studentProfileRepository.countByUserDepartmentIdAndIsPlacedTrue(departmentId);

        long ongoingDrives = placementDriveRepository.countByAllowedDepartmentIdAndStatus(departmentId,
                DriveStatus.ONGOING);

        // We can just set active applications to 0 for the generic stat or count it if
        // needed. Leaving as 0 or omitting for now. (Not explicitly asked in breakdown
        // but good to have)

        double placementPercentage = totalStudents > 0 ? ((double) placedStudents / totalStudents) * 100 : 0.0;

        // Round to 2 decimal places
        placementPercentage = Math.round(placementPercentage * 100.0) / 100.0;

        return FacultyDashboardStatsDTO.builder()
                .totalStudents(totalStudents)
                .verifiedStudents(verifiedStudents)
                .pendingVerifications(pendingVerifications)
                .placedStudents(placedStudents)
                .placementPercentage(placementPercentage)
                .ongoingDrives(ongoingDrives)
                .activeApplications(0L) // Placeholder if not needed globally
                .build();
    }
}
