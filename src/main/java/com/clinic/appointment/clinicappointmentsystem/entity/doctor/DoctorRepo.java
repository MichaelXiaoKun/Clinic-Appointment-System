package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepo extends JpaRepository<DoctorEntity, String> {
    List<DoctorEntity> findDoctorEntitiesByFirstNameAndLastName(String firstName, String lastName);
}