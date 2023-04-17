package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepo;

    public Optional<PatientEntity> getPatientByUsername(String username) {
        return patientRepo.findById(username);
    }

    public PatientEntity savePatient(PatientEntity patientEntity) {
        return patientRepo.save(patientEntity);
    }

    public void deletePatient(String username) {
        patientRepo.deleteById(username);
    }
}
