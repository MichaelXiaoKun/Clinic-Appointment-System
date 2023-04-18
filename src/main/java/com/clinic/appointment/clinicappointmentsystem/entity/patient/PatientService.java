package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.exception.DoctorNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.PatientFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.PatientNotFoundException;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public List<PatientEntity> getAllPatientProfiles() {
        return patientRepo.findAll();
    }

    public PatientEntity getPatientByUsername(String username) throws PatientNotFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientNotFoundException("Patient with username: " + username + " not found");
        }
        return patient.get();
    }

    public void createPatient(PatientEntity patientEntity) throws PatientFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(patientEntity.getUsername());
        if (patient.isPresent()) {
            throw new PatientFoundException("Patient with username: " + patientEntity.getUsername() + " already exists");
        }
        patientRepo.save(patientEntity);
    }

    public void deletePatientByUsername(String username) throws PatientNotFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientNotFoundException("Patient with username: \" + username + \" not found");
        }

        patientRepo.deleteById(username);
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
                              String emergencyPhoneNumber) throws PatientNotFoundException {

        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientNotFoundException("Patient with username: " + username + " not found");
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
