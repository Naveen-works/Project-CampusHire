package com.example.backend.Services;

import com.example.backend.DTOs.StudentProfileDto;
import com.example.backend.Exceptions.ProfileLockedException;
import com.example.backend.Exceptions.ResourceNotFoundException;
import com.example.backend.Mappers.StudentMapper;
import com.example.backend.Models.*;
import com.example.backend.Models.enums.VerificationStatus;
import com.example.backend.Repositories.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public StudentProfileDto getProfileByEmail(String email) {
        StudentProfile profile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return StudentMapper.toDto(profile);
    }

    private StudentProfile getEditableProfile(String email) {
        StudentProfile profile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (Boolean.TRUE.equals(profile.getIsLocked())
                || profile.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new ProfileLockedException("Profile is locked or verified and cannot be edited");
        }
        return profile;
    }

    @Transactional
    public void updatePersonalDetails(String email, StudentProfileDto.PersonalDetailsDto details) {
        StudentProfile profile = getEditableProfile(email);

        PersonalDetails pd = profile.getPersonalDetails();
        if (pd == null) {
            pd = new PersonalDetails();
            pd.setStudentProfile(profile);
            profile.setPersonalDetails(pd);
        }

        pd.setFirstName(details.getFirstName());
        pd.setLastName(details.getLastName());
        pd.setGender(details.getGender());
        pd.setDateOfBirth(details.getDateOfBirth());
        pd.setBio(details.getBio());

        studentProfileRepository.save(profile);
    }

    @Transactional
    public void updateContactDetails(String email, StudentProfileDto.ContactDetailsDto details) {
        StudentProfile profile = getEditableProfile(email);

        ContactDetails cd = profile.getContactDetails();
        if (cd == null) {
            cd = new ContactDetails();
            cd.setStudentProfile(profile);
            profile.setContactDetails(cd);
        }

        cd.setAlternateEmail(details.getAlternateEmail());
        cd.setStudentMobile1(details.getStudentMobile1());
        cd.setCity(details.getCity());
        cd.setState(details.getState());

        studentProfileRepository.save(profile);
    }

    @Transactional
    public void updateAcademicRecord(String email, StudentProfileDto.AcademicRecordDto details) {
        StudentProfile profile = getEditableProfile(email);

        AcademicRecord ar = profile.getAcademicRecord();
        if (ar == null) {
            ar = new AcademicRecord();
            ar.setStudentProfile(profile);
            profile.setAcademicRecord(ar);
        }

        ar.setCgpa(details.getCgpa());
        ar.setStandingArrears(details.getStandingArrears());
        ar.setHistoryOfArrears(details.getHistoryOfArrears());
        ar.setUgYearOfPass(details.getUgYearOfPass());

        studentProfileRepository.save(profile);
    }

    @Transactional
    public void updateSchoolingDetails(String email, StudentProfileDto.SchoolingDetailsDto details) {
        StudentProfile profile = getEditableProfile(email);

        SchoolingDetails sd = profile.getSchoolingDetails();
        if (sd == null) {
            sd = new SchoolingDetails();
            sd.setStudentProfile(profile);
            profile.setSchoolingDetails(sd);
        }

        sd.setXMarksPercentage(details.getXMarksPercentage());
        sd.setXiiMarksPercentage(details.getXiiMarksPercentage());

        studentProfileRepository.save(profile);
    }

    public String getVerificationStatus(String email) {
        StudentProfile profile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profile.getVerificationStatus().name();
    }
}
