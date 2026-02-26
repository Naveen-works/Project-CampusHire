package com.example.backend.DTOs;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {
    private Long id;
    private String rollNo;
    private String batch;
    private String resumeUrl;
    private String verificationStatus;
    private Boolean isLocked;
    private Boolean isEligibleForPlacements;
    private Boolean interestedOnPlacement;

    private Boolean isPlaced;
    private Integer numberOfOffers;
    private Double highestPackageLpa;

    private PersonalDetailsDto personalDetails;
    private ContactDetailsDto contactDetails;
    private AcademicRecordDto academicRecord;
    private SchoolingDetailsDto schoolingDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalDetailsDto {
        private String firstName;
        private String lastName;
        private String gender;
        private LocalDate dateOfBirth;
        private String bio;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDetailsDto {
        private String alternateEmail;
        private String studentMobile1;
        private String city;
        private String state;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcademicRecordDto {
        private Double cgpa;
        private Integer standingArrears;
        private Integer historyOfArrears;
        private Integer ugYearOfPass;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchoolingDetailsDto {
        private Double xMarksPercentage;
        private Double xiiMarksPercentage;
    }
}
