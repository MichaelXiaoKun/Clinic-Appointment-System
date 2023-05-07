package com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface DoctorBreaksRepo extends JpaRepository<DoctorBreaksEntity, Integer> {
    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByDoctorUsername(String username);
    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByStartTimeAfterAndEndTimeBefore(Timestamp startTime, Timestamp endTime);
}
