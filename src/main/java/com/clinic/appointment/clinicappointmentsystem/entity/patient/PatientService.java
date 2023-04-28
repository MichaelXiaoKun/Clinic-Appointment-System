package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.LoginAttemptService;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PatientUsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepo patientRepo;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public PatientService(PatientRepo patientRepo, PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    public List<PatientEntity> getAllPatientProfiles() {
        return patientRepo.findAll();
    }

    public PatientEntity getPatientByUsername(String username) throws PatientUsernameNotFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientUsernameNotFoundException("Patient with username: " + username + " not found");
        }
        return patient.get();
    }

    public PatientEntity resetPassword(String username, String new_password) {
        PatientEntity foundPatient = patientRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("Doctor with username " + username + " not found")
        );
        foundPatient.setPassword(passwordEncoder.encode(new_password));
        patientRepo.save(foundPatient);
        return foundPatient;
    }

    private void validateLoginAttempt(PatientEntity patientEntity) {

        if (patientEntity.isAccountNonLocked()) {
////            if (loginAttemptService.attemptsGreaterThanAllowed(patientEntity.getUsername())) {
////                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO TRUE
////            } else {
////                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO FALSE
////            }
        } else {
            loginAttemptService.deleteUserFromLoginAttemptCache(patientEntity.getUsername());
        }
    }

    public void deletePatientByUsername(String username) throws PatientUsernameNotFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientUsernameNotFoundException("Patient with username: \" + username + \" not found");
        }

        patientRepo.deleteById(username);
    }

    private void validateLoginAttempt(DoctorEntity doctorEntity) {

        if (doctorEntity.isAccountNonLocked()) {
////            if (loginAttemptService.attemptsGreaterThanAllowed(doctorEntity.getUsername())) {
////                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO TRUE
////            } else {
////                //TODO: MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO FALSE
////            }
        } else {
            loginAttemptService.deleteUserFromLoginAttemptCache(doctorEntity.getUsername());
        }
    }

    @Transactional
    public void updatePatient(String username,
                              String firstName,
                              String lastName,
                              String phoneNumber,
                              Date dob,
                              String middleName,
                              String email,
                              String insuranceProvider,
                              String policyNumber,
                              String groupNumber,
                              Integer balance,
                              String insurancePhone,
                              String emergencyFirstName,
                              String emergencyLastName,
                              String emergencyPhoneNumber) throws PatientUsernameNotFoundException {

        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientUsernameNotFoundException("Patient with username: " + username + " not found");
        }

        PatientEntity foundPatient = patient.get();

        if (firstName != null) {
            foundPatient.setFirstName(firstName);
        }
        if (lastName != null) {
            foundPatient.setLastName(lastName);
        }
        if (phoneNumber != null) {
            foundPatient.setPhoneNumber(phoneNumber);
        }
        if (dob != null) {
            foundPatient.setDob(dob);
        }
        if (middleName != null) {
            foundPatient.setMiddleName(middleName);
        }
        if (email != null) {
            foundPatient.setEmail(email);
        }
        if (insuranceProvider != null) {
            foundPatient.setInsuranceProvider(insuranceProvider);
        }
        if (policyNumber != null) {
            foundPatient.setPolicyNumber(policyNumber);
        }
        if (groupNumber != null) {
            foundPatient.setGroupNumber(groupNumber);
        }
        if (balance != null) {
            foundPatient.setBalance(balance);
        }
        if (insurancePhone != null) {
            foundPatient.setInsurancePhone(insurancePhone);
        }
        if (emergencyFirstName != null) {
            foundPatient.setEmergencyFirstName(emergencyFirstName);
        }
        if (emergencyLastName != null) {
            foundPatient.setEmergencyLastName(emergencyLastName);
        }
        if (emergencyPhoneNumber != null) {
            foundPatient.setEmergencyPhoneNumber(emergencyPhoneNumber);
        }

        patientRepo.save(foundPatient);
    }
}
