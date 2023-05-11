package com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks;

import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface DoctorBreaksRepo extends JpaRepository<DoctorBreaksEntity, Integer> {
    boolean existsDoctorBreaksEntitiesByDoctorUsernameAndStartTimeAndEndTime(String doctorName, Timestamp startTime, Timestamp endTime);
    void deleteDoctorBreaksEntitiesByDoctorUsernameAndStartTimeAndEndTime(String doctorName, Timestamp startTime, Timestamp endTime);
    void deleteDoctorBreaksEntitiesByDoctorUsername(String username);

    DoctorBreaksEntity findDoctorBreaksEntityByDoctorUsernameAndStartTimeAndEndTime(String username, Timestamp startTime, Timestamp endTime);

    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByDoctorUsername(String username);
    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByDoctorUsernameAndStartTimeBefore(String username, Timestamp time);
    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByDoctorUsernameAndStartTimeAfterAndEndTimeBefore(String username, Timestamp startTime, Timestamp endTime);
    List<DoctorBreaksEntity> findDoctorBreaksEntitiesByDoctorUsernameAndEndTimeAfterAndStartTimeBefore(String username, Timestamp startTime, Timestamp endTime);
}

