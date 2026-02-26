package com.example.backend.Models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drive_eligibility")
public class EligibilityCriteria {
    @Id
    private Long driveId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "drive_id")
    private PlacementDrive drive;

    private Double minCgpa;
    private Double minXMarks;
    private Double minXiiMarks;
    private Integer maxStandingArrears;
    private Integer maxHistoryOfArrears;

    @ManyToMany
    @JoinTable(name = "drive_allowed_departments", joinColumns = @JoinColumn(name = "drive_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
    private List<Department> allowedDepartments;
}
