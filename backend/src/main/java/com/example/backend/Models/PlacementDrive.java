package com.example.backend.Models;

import com.example.backend.Models.enums.DriveStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "placement_drives")
public class PlacementDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String title;
    private String role;
    private Double ctcLpa;

    @Enumerated(EnumType.STRING)
    private DriveStatus status; // UPCOMING, ONGOING, COMPLETED

    @OneToOne(mappedBy = "drive", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EligibilityCriteria eligibilityCriteria;
}
