package com.clinic.appointment.clinicappointmentsystem.entity.patient;



import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PatientRepo extends JpaRepository<PatientEntity, String> {

    List<PatientEntity> findPatientEntitiesByFirstNameAndLastName(String firstName, String lastName);
    List<PatientEntity> findPatientEntitiesByFirstName(String firstName);
    List<PatientEntity> findPatientEntitiesByLastName(String lastName);
    List<PatientEntity> findPatientEntitiesByBalanceGreaterThanEqual(int threshold);

    List<PatientEntity> findPatientEntitiesByEmail(String email);
}
