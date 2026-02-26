package com.example.backend.Controllers;

import com.example.backend.DTOs.StudentProfileDto;
import com.example.backend.Services.StudentProfileService;
import com.example.backend.Utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/profile")
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;

    // TODO: In a real app with Spring Security, this email would come from the
    // SecurityContext
    // For now, passing it as a Query Param to mock the logged-in user

    @GetMapping
    public ResponseEntity<ApiResponse<StudentProfileDto>> getMyProfile(@RequestParam String email) {
        return ResponseEntity.ok(
                ApiResponse.success("Profile fetched successfully", studentProfileService.getProfileByEmail(email)));
    }

    @GetMapping("/verification-status")
    public ResponseEntity<ApiResponse<String>> getVerificationStatus(@RequestParam String email) {
        return ResponseEntity
                .ok(ApiResponse.success("Status fetched", studentProfileService.getVerificationStatus(email)));
    }

    @PutMapping("/personal")
    public ResponseEntity<ApiResponse<Void>> updatePersonalDetails(@RequestParam String email,
            @RequestBody StudentProfileDto.PersonalDetailsDto details) {
        studentProfileService.updatePersonalDetails(email, details);
        return ResponseEntity.ok(ApiResponse.success("Personal details updated", null));
    }

    @PutMapping("/contact")
    public ResponseEntity<ApiResponse<Void>> updateContactDetails(@RequestParam String email,
            @RequestBody StudentProfileDto.ContactDetailsDto details) {
        studentProfileService.updateContactDetails(email, details);
        return ResponseEntity.ok(ApiResponse.success("Contact details updated", null));
    }

    @PutMapping("/academic")
    public ResponseEntity<ApiResponse<Void>> updateAcademicRecord(@RequestParam String email,
            @RequestBody StudentProfileDto.AcademicRecordDto details) {
        studentProfileService.updateAcademicRecord(email, details);
        return ResponseEntity.ok(ApiResponse.success("Academic record updated", null));
    }

    @PutMapping("/schooling")
    public ResponseEntity<ApiResponse<Void>> updateSchoolingDetails(@RequestParam String email,
            @RequestBody StudentProfileDto.SchoolingDetailsDto details) {
        studentProfileService.updateSchoolingDetails(email, details);
        return ResponseEntity.ok(ApiResponse.success("Schooling details updated", null));
    }
}
