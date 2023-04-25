package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import com.clinic.appointment.clinicappointmentsystem.exception.AppointmentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepo appointmentRepo;

    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public AppointmentEntity getAppointmentByID(int apptID) throws AppointmentNotFoundException {
        Optional<AppointmentEntity> appt = appointmentRepo.findById(apptID);
        if (appt.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment with ID: " + String.valueOf(apptID) + " is not found");
        }
        return appt.get();
    }

    public List<AppointmentEntity> getAppointmentsByDoctorUsername(String doctorUsername) {
        List<AppointmentEntity> appts = appointmentRepo.findAppointmentEntitiesByDoctorUsername(doctorUsername);
        return appts;
    }

    public List<AppointmentEntity> getAppointmentsByPatientUsername(String patientUsername) {
        List<AppointmentEntity> appts = appointmentRepo.findAppointmentEntitiesByPatientUsername(patientUsername);
        return appts;
    }

    public List<AppointmentEntity> getAppointmentsBtwStartTimeAndEndTime(Timestamp start_time, Timestamp end_time) {
        return appointmentRepo.findAppointmentEntitiesByStartDateAfterAndEndDateBefore(start_time, end_time);
    }
}
