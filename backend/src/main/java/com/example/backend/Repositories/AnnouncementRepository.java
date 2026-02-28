package com.example.backend.Repositories;

import com.example.backend.Models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    java.util.List<Announcement> findByDepartmentIdOrderByCreatedAtDesc(Long departmentId);

    java.util.List<Announcement> findByDepartmentIdOrScopeOrderByCreatedAtDesc(Long departmentId,
            com.example.backend.Models.enums.AnnouncementScope scope);
}
