package com.example.backend.Services;

import com.example.backend.DTOs.PlacementDriveDto;
import com.example.backend.Exceptions.ResourceNotFoundException;
import com.example.backend.Mappers.PlacementMapper;
import com.example.backend.Models.AcademicRecord;
import com.example.backend.Models.EligibilityCriteria;
import com.example.backend.Models.PlacementDrive;
import com.example.backend.Models.SchoolingDetails;
import com.example.backend.Models.StudentProfile;
import com.example.backend.Models.enums.DriveStatus;
import com.example.backend.Repositories.PlacementDriveRepository;
import com.example.backend.Repositories.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentDriveService {

    @Autowired
    private PlacementDriveRepository placementDriveRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public List<PlacementDriveDto> getVisibleDrives(String email) {
        StudentProfile profile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<PlacementDrive> drives = placementDriveRepository
                .findByStatusIn(List.of(DriveStatus.ONGOING, DriveStatus.UPCOMING));

        return drives.stream().map(drive -> {
            boolean isEligible = true;
            String ineligibilityReason = null;

            EligibilityCriteria ec = drive.getEligibilityCriteria();
            AcademicRecord ar = profile.getAcademicRecord();
            SchoolingDetails sd = profile.getSchoolingDetails();

            if (ec != null) {
                if (ar == null || sd == null) {
                    isEligible = false;
                    ineligibilityReason = "Academic or Schooling details are incomplete.";
                } else {
                    if (ec.getMinCgpa() != null && (ar.getCgpa() == null || ar.getCgpa() < ec.getMinCgpa())) {
                        isEligible = false;
                        ineligibilityReason = "CGPA is below the minimum requirement.";
                    } else if (ec.getMaxStandingArrears() != null && (ar.getStandingArrears() == null
                            || ar.getStandingArrears() > ec.getMaxStandingArrears())) {
                        isEligible = false;
                        ineligibilityReason = "Standing arrears exceed the maximum allowed.";
                    } else if (ec.getMaxHistoryOfArrears() != null && (ar.getHistoryOfArrears() == null
                            || ar.getHistoryOfArrears() > ec.getMaxHistoryOfArrears())) {
                        isEligible = false;
                        ineligibilityReason = "History of arrears exceeds the maximum allowed.";
                    } else if (ec.getMinXMarks() != null
                            && (sd.getXMarksPercentage() == null || sd.getXMarksPercentage() < ec.getMinXMarks())) {
                        isEligible = false;
                        ineligibilityReason = "10th marks are below the required percentage.";
                    } else if (ec.getMinXiiMarks() != null && (sd.getXiiMarksPercentage() == null
                            || sd.getXiiMarksPercentage() < ec.getMinXiiMarks())) {
                        isEligible = false;
                        ineligibilityReason = "12th marks are below the required percentage.";
                    }
                }
            }

            return PlacementMapper.toDriveDto(drive, isEligible, ineligibilityReason);
        }).collect(Collectors.toList());
    }

    public PlacementDriveDto getDriveDetails(String email, Long driveId) {
        // Find if eligible just for this one drive
        List<PlacementDriveDto> allDrives = getVisibleDrives(email);
        return allDrives.stream()
                .filter(d -> d.getId().equals(driveId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found or not visible"));
    }
}
