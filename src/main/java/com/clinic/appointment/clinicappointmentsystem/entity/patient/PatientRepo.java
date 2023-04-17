package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PatientRepo extends JpaRepository<PatientEntity, String> {

    Optional<List<PatientEntity>> findPatientEntitiesByFirstNameAndLastName(String firstName, String lastName);
}
