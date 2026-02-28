package com.example.backend.Controllers;

import com.example.backend.AOPs.AuditAction;
import com.example.backend.DTOs.Faculty.FacultyDashboardStatsDTO;
import com.example.backend.Services.FacultyDashboardService;
import com.example.backend.Utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty/dashboard")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FacultyDashboardController {

    @Autowired
    private FacultyDashboardService facultyDashboardService;

    @AuditAction(action = "VIEW_FACULTY_DASHBOARD", targetEntity = "DASHBOARD_STATS")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<FacultyDashboardStatsDTO>> getDashboardStats(
            @RequestParam(required = false, defaultValue = "faculty@dept.com") String facultyEmail) {

        FacultyDashboardStatsDTO stats = facultyDashboardService.getDepartmentStats(facultyEmail);
        return ResponseEntity.ok(ApiResponse.success("Faculty Dashboard stats retrieved successfully", stats));
    }
}
