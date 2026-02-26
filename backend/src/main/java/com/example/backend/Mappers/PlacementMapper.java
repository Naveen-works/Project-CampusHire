package com.example.backend.Mappers;

import com.example.backend.DTOs.DriveApplicationDto;
import com.example.backend.DTOs.PlacementDriveDto;
import com.example.backend.Models.DriveApplication;
import com.example.backend.Models.PlacementDrive;

public class PlacementMapper {

    public static PlacementDriveDto toDriveDto(PlacementDrive drive, boolean isEligible, String ineligibilityReason) {
        if (drive == null)
            return null;

        PlacementDriveDto dto = PlacementDriveDto.builder()
                .id(drive.getId())
                .title(drive.getTitle())
                .role(drive.getRole())
                .ctcLpa(drive.getCtcLpa())
                .status(drive.getStatus() != null ? drive.getStatus().name() : null)
                .isEligible(isEligible)
                .ineligibilityReason(ineligibilityReason)
                .build();

        if (drive.getCompany() != null) {
            dto.setCompanyName(drive.getCompany().getName());
            dto.setCompanyIndustry(drive.getCompany().getIndustry());
            dto.setCompanyWebsite(drive.getCompany().getWebsite());
        }

        return dto;
    }

    public static DriveApplicationDto toAppDto(DriveApplication app) {
        if (app == null)
            return null;

        DriveApplicationDto dto = DriveApplicationDto.builder()
                .id(app.getId())
                .stage(app.getStage() != null ? app.getStage().name() : null)
                .appliedAt(app.getAppliedAt())
                .lastUpdatedAt(app.getLastUpdatedAt())
                .build();

        if (app.getDrive() != null) {
            dto.setDriveId(app.getDrive().getId());
            dto.setDriveTitle(app.getDrive().getTitle());
            if (app.getDrive().getCompany() != null) {
                dto.setCompanyName(app.getDrive().getCompany().getName());
            }
        }

        return dto;
    }
}
