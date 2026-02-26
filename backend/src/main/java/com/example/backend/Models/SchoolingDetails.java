package com.example.backend.Models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schooling_details")
public class SchoolingDetails {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private StudentProfile studentProfile;

    // 10th
    private Double xMarksPercentage;
    private Integer xYearOfPassing;
    private String xSchoolName;
    private String xBoardOfStudy;

    // 12th
    private Double xiiMarksPercentage;
    private Integer xiiYearOfPassing;
    private String xiiSchoolName;
    private String xiiBoardOfStudy;
    private Double xiiCutOffMarks;

    // Diploma
    private Double diplomaMarksPercentage;
}
