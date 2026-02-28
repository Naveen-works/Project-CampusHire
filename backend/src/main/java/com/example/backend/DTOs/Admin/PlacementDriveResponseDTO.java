package com.example.backend.DTOs.Admin;

import com.example.backend.Models.enums.DriveStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlacementDriveResponseDTO {
    private Long id;
    private Long companyId;
    private String companyName;
    private String title;
    private String role;
    private Double ctcLpa;
    private DriveStatus status;
}
