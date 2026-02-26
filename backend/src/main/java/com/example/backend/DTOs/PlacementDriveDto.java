package com.example.backend.DTOs;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacementDriveDto {
    private Long id;
    private String title;
    private String role;
    private Double ctcLpa;
    private String status; // UPCOMING, ONGOING, COMPLETED

    private String companyName;
    private String companyIndustry;
    private String companyWebsite;

    private Boolean isEligible;
    private String ineligibilityReason;
}
