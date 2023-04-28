package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<AppointmentEntity, Integer> {
    List<AppointmentEntity> findAppointmentEntitiesByStartDateAfterAndEndDateBefore(Timestamp startDate, Timestamp endDate);

    List<AppointmentEntity> findAppointmentEntitiesByPatientUsername(String patientUsername);

    List<AppointmentEntity> findAppointmentEntitiesByDoctorUsername(String doctorUsername);
}
