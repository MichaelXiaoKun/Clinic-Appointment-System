package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepo extends JpaRepository<DoctorEntity, String> {
    List<DoctorEntity> findDoctorEntitiesByFirstNameAndLastName(String firstName, String lastName);
    List<DoctorEntity> findDoctorEntitiesByFirstName(String firstName);
    List<DoctorEntity> findDoctorEntitiesByLastName(String lastName);
    List<DoctorEntity> findDoctorEntitiesBySpecialty(String specialty);
    List<DoctorEntity> findDoctorEntitiesByDegree(String degree);
    List<DoctorEntity> findDoctorEntitiesByLicenseNumber(String licenseNumber);
    List<DoctorEntity> findDoctorEntitiesByBoardCertification(String boardCertification);

    List<DoctorEntity> findDoctorEntitiesByEmail(String email);
}
