package com.example.backend.Services;

import com.example.backend.DTOs.Admin.AnnouncementRequestDTO;
import com.example.backend.DTOs.Admin.AnnouncementResponseDTO;
import com.example.backend.Exceptions.ResourceNotFoundException;
import com.example.backend.Models.Announcement;
import com.example.backend.Models.Department;
import com.example.backend.Models.User;
import com.example.backend.Models.enums.AnnouncementScope;
import com.example.backend.Repositories.AnnouncementRepository;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminAnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AnnouncementResponseDTO createAnnouncement(AnnouncementRequestDTO request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        Department department = null;
        if (request.getScope() == AnnouncementScope.DEPARTMENT) {
            if (request.getDepartmentId() == null) {
                throw new IllegalArgumentException("Department ID is required for DEPARTMENT scope");
            }
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .scope(request.getScope())
                .department(department)
                .createdBy(admin)
                .createdAt(LocalDateTime.now())
                .build();

        announcement = announcementRepository.save(announcement);
        return mapToDTO(announcement);
    }

    public List<AnnouncementResponseDTO> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Announcement not found");
        }
        announcementRepository.deleteById(id);
    }

    private AnnouncementResponseDTO mapToDTO(Announcement announcement) {
        return AnnouncementResponseDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .scope(announcement.getScope())
                .departmentName(announcement.getDepartment() != null ? announcement.getDepartment().getName() : null)
                .createdByEmail(announcement.getCreatedBy() != null ? announcement.getCreatedBy().getEmail() : "System")
                .createdAt(announcement.getCreatedAt())
                .build();
    }
}
