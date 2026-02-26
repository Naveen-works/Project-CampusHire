package com.example.backend.Mappers;

import com.example.backend.DTOs.StudentProfileDto;
import com.example.backend.Models.StudentProfile;

public class StudentMapper {

    public static StudentProfileDto toDto(StudentProfile profile) {
        if (profile == null)
            return null;

        StudentProfileDto dto = StudentProfileDto.builder()
                .id(profile.getId())
                .rollNo(profile.getRollNo())
                .batch(profile.getBatch())
                .resumeUrl(profile.getResumeUrl())
                .verificationStatus(
                        profile.getVerificationStatus() != null ? profile.getVerificationStatus().name() : null)
                .isLocked(profile.getIsLocked())
                .isEligibleForPlacements(profile.getIsEligibleForPlacements())
                .interestedOnPlacement(profile.getInterestedOnPlacement())
                .isPlaced(profile.getIsPlaced())
                .numberOfOffers(profile.getNumberOfOffers())
                .highestPackageLpa(profile.getHighestPackageLpa())
                .build();

        if (profile.getPersonalDetails() != null) {
            StudentProfileDto.PersonalDetailsDto pd = new StudentProfileDto.PersonalDetailsDto();
            pd.setFirstName(profile.getPersonalDetails().getFirstName());
            pd.setLastName(profile.getPersonalDetails().getLastName());
            pd.setGender(profile.getPersonalDetails().getGender());
            pd.setDateOfBirth(profile.getPersonalDetails().getDateOfBirth());
            pd.setBio(profile.getPersonalDetails().getBio());
            dto.setPersonalDetails(pd);
        }

        if (profile.getContactDetails() != null) {
            StudentProfileDto.ContactDetailsDto cd = new StudentProfileDto.ContactDetailsDto();
            cd.setAlternateEmail(profile.getContactDetails().getAlternateEmail());
            cd.setStudentMobile1(profile.getContactDetails().getStudentMobile1());
            cd.setCity(profile.getContactDetails().getCity());
            cd.setState(profile.getContactDetails().getState());
            dto.setContactDetails(cd);
        }

        if (profile.getAcademicRecord() != null) {
            StudentProfileDto.AcademicRecordDto ar = new StudentProfileDto.AcademicRecordDto();
            ar.setCgpa(profile.getAcademicRecord().getCgpa());
            ar.setStandingArrears(profile.getAcademicRecord().getStandingArrears());
            ar.setHistoryOfArrears(profile.getAcademicRecord().getHistoryOfArrears());
            ar.setUgYearOfPass(profile.getAcademicRecord().getUgYearOfPass());
            dto.setAcademicRecord(ar);
        }

        if (profile.getSchoolingDetails() != null) {
            StudentProfileDto.SchoolingDetailsDto sd = new StudentProfileDto.SchoolingDetailsDto();
            sd.setXMarksPercentage(profile.getSchoolingDetails().getXMarksPercentage());
            sd.setXiiMarksPercentage(profile.getSchoolingDetails().getXiiMarksPercentage());
            dto.setSchoolingDetails(sd);
        }

        return dto;
    }
}
