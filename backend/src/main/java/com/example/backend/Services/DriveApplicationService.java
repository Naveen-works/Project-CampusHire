package com.example.backend.Services;

import com.example.backend.DTOs.DriveApplicationDto;
import com.example.backend.DTOs.PlacementDriveDto;
import com.example.backend.Exceptions.DuplicateApplicationException;
import com.example.backend.Exceptions.ResourceNotFoundException;
import com.example.backend.Exceptions.UnauthorizedException;
import com.example.backend.Mappers.PlacementMapper;
import com.example.backend.Models.DriveApplication;
import com.example.backend.Models.PlacementDrive;
import com.example.backend.Models.StudentProfile;
import com.example.backend.Models.enums.ApplicationStage;
import com.example.backend.Models.enums.VerificationStatus;
import com.example.backend.Models.enums.DriveStatus;
import com.example.backend.Repositories.DriveApplicationRepository;
import com.example.backend.Repositories.PlacementDriveRepository;
import com.example.backend.Repositories.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriveApplicationService {

    @Autowired
    private DriveApplicationRepository applicationRepository;

    @Autowired
    private PlacementDriveRepository driveRepository;

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private StudentDriveService studentDriveService;

    public void applyForDrive(String email, Long driveId) {
        StudentProfile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (profile.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new UnauthorizedException("Cannot apply. Profile is not verified by faculty.");
        }

        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));

        if (drive.getStatus() != DriveStatus.ONGOING) {
            throw new IllegalArgumentException("Drive is not currently ongoing.");
        }

        // Check duplicates
        boolean alreadyApplied = applicationRepository.existsByStudentProfileIdAndDriveId(profile.getId(), driveId);
        if (alreadyApplied) {
            throw new DuplicateApplicationException("You have already applied for this drive.");
        }

        // Check eligibility via service logic
        PlacementDriveDto evaluatedDrive = studentDriveService.getDriveDetails(email, driveId);
        if (!Boolean.TRUE.equals(evaluatedDrive.getIsEligible())) {
            throw new IllegalArgumentException(
                    "You are not eligible to apply: " + evaluatedDrive.getIneligibilityReason());
        }

        DriveApplication application = DriveApplication.builder()
                .studentProfile(profile)
                .drive(drive)
                .stage(ApplicationStage.APPLIED)
                .appliedAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        applicationRepository.save(application);
    }

    public List<DriveApplicationDto> getMyApplications(String email) {
        StudentProfile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<DriveApplication> applications = applicationRepository.findByStudentProfileId(profile.getId());

        return applications.stream()
                .map(PlacementMapper::toAppDto)
                .collect(Collectors.toList());
    }
}
