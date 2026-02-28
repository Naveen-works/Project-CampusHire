package com.example.backend.Controllers;

import com.example.backend.AOPs.AuditAction;
import com.example.backend.DTOs.Admin.AnnouncementRequestDTO;
import com.example.backend.DTOs.Admin.AnnouncementResponseDTO;
import com.example.backend.Services.AdminAnnouncementService;
import com.example.backend.Utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/announcements")
public class AdminAnnouncementController {

    @Autowired
    private AdminAnnouncementService announcementService;

    @PostMapping
    @AuditAction(action = "CREATE_ANNOUNCEMENT", targetEntity = "Announcement")
    public ResponseEntity<ApiResponse<AnnouncementResponseDTO>> createAnnouncement(
            @RequestParam(required = false, defaultValue = "admin@campushire.com") String email,
            @Valid @RequestBody AnnouncementRequestDTO request) {
        AnnouncementResponseDTO response = announcementService.createAnnouncement(request, email);
        return ResponseEntity.ok(ApiResponse.success("Announcement created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnnouncementResponseDTO>>> getAllAnnouncements() {
        return ResponseEntity.ok(
                ApiResponse.success("Announcements fetched successfully", announcementService.getAllAnnouncements()));
    }

    @DeleteMapping("/{id}")
    @AuditAction(action = "DELETE_ANNOUNCEMENT", targetEntity = "Announcement")
    public ResponseEntity<ApiResponse<String>> deleteAnnouncement(
            @RequestParam(required = false) String email,
            @PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(ApiResponse.success("Announcement deleted successfully", null));
    }
}
