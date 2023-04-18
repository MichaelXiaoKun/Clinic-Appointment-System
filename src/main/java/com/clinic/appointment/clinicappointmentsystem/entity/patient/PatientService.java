package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.exception.DoctorNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.PatientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public PatientEntity getPatientByUsername(String username) throws PatientNotFoundException {
        Optional<PatientEntity> patient = patientRepo.findById(username);
        if (patient.isEmpty()) {
            throw new PatientNotFoundException("Patient with username: " + username + " not found");
        }
        return patient.get();
    }

    public PatientEntity savePatient(PatientEntity patientEntity) {
        return patientRepo.save(patientEntity);
    }

    public void deletePatient(String username) {
        patientRepo.deleteById(username);
    }
}
