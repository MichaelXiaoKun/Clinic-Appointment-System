package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends JpaRepository<DoctorEntity, String> {

    Optional<List<DoctorEntity>> findDoctorEntitiesByFirstNameAndLastName(String firstName, String lastName);
}
