package com.example.backend.DTOs.Faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyDashboardStatsDTO {
    private Long totalStudents;
    private Long verifiedStudents;
    private Long pendingVerifications;
    private Long placedStudents;
    private Double placementPercentage;
    private Long ongoingDrives;
    private Long activeApplications;
}
