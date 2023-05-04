package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.LoginAttemptService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepo doctorRepo;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorService(DoctorRepo doctorRepo, LoginAttemptService loginAttemptService, PasswordEncoder passwordEncoder) {
        this.doctorRepo = doctorRepo;
        this.loginAttemptService = loginAttemptService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<DoctorEntity> getAllDoctorProfiles() {
        return doctorRepo.findAll();
    }

    public DoctorEntity getDoctorByUsername(String username) throws DoctorUsernameNotFoundException {
        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found");
        }
        return doctor.get();
    }

    public List<DoctorEntity> getDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        return doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName);
    }

    public List<DoctorEntity> getDoctorsByFirstName(String firstName) {
        return doctorRepo.findDoctorEntitiesByFirstName(firstName);
    }

    public List<DoctorEntity> getDoctorsByLastName(String lastName) {
        return doctorRepo.findDoctorEntitiesByLastName(lastName);
    }

    public List<DoctorEntity> getDoctorsBySpecialty(String specialty) {
        return doctorRepo.findDoctorEntitiesBySpecialty(specialty);
    }

    public List<DoctorEntity> getDoctorsByDegree(String degree) {
        return doctorRepo.findDoctorEntitiesByDegree(degree);
    }

    public void resetPassword(String username, String new_password) {
        DoctorEntity foundDoctor = doctorRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("Doctor with username " + username + " not found")
        );

        foundDoctor.setPassword(passwordEncoder.encode(new_password));
        doctorRepo.save(foundDoctor);
    }

    private void validateLoginAttempt(DoctorEntity doctorEntity) {

        if (doctorEntity.isAccountNonLocked()) {
////            if (loginAttemptService.attemptsGreaterThanAllowed(doctorEntity.getUsername())) {
//                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO TRUE
////            } else {
//                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO FALSE
////            }
        } else {
            loginAttemptService.deleteUserFromLoginAttemptCache(doctorEntity.getUsername());
        }
    }

    @Transactional
    public void updateDoctor(String username,
                             String firstName,
                             String lastName,
                             String phoneNumber,
                             Date dob,
                             String middleName,
                             String email,
                             String specialty,
                             String degree,
                             String licenseNumber,
                             String boardCertification) throws DoctorUsernameNotFoundException {

        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found");
        }

        DoctorEntity foundDoctor = doctor.get();

        if (firstName != null) {
            foundDoctor.setFirstName(firstName);
        }
        if (lastName != null) {
            foundDoctor.setLastName(lastName);
        }
        if (phoneNumber != null) {
            foundDoctor.setPhoneNumber(phoneNumber);
        }
        if (dob != null) {
            foundDoctor.setDob(dob);
        }
        if (middleName != null) {
            foundDoctor.setMiddleName(middleName);
        }
        if (email != null) {
            foundDoctor.setEmail(email);
        }
        if (specialty != null) {
            foundDoctor.setSpecialty(specialty);
        }
        if (degree != null) {
            foundDoctor.setDegree(degree);
        }
        if (licenseNumber != null) {
            foundDoctor.setLicenseNumber(licenseNumber);
        }
        if (boardCertification != null) {
            foundDoctor.setBoardCertification(boardCertification);
        }
        doctorRepo.save(foundDoctor);
    }

    public void deleteDoctor(String username, String password)
            throws DoctorUsernameNotFoundException, PasswordMismatchException {
        DoctorEntity doctor = doctorRepo.findById(username).orElseThrow(
                ()-> new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found")
        );

        String encoded_user_pwd = passwordEncoder.encode(password);
        if (!doctor.getPassword().equals(encoded_user_pwd)) {
            throw new PasswordMismatchException("Password is incorrect");
        }

        doctorRepo.deleteById(username);
    }

    public List<DoctorEntity> getDoctorsByLicenseNumber(String licenseNumber) {
        return doctorRepo.findDoctorEntitiesByLicenseNumber(licenseNumber);
    }

    public List<DoctorEntity> getDoctorsByBoardCertification(String boardCertification) {
        return doctorRepo.findDoctorEntitiesByBoardCertification(boardCertification);
    }
}
