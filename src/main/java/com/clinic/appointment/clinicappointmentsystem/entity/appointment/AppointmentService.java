package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import com.clinic.appointment.clinicappointmentsystem.entity.appointment.Handler.AppointmentHandler;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService{

    private AppointmentHandler dailyHandler;
    private final AppointmentRepo appointmentRepo;

    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo, AppointmentHandler dailyHandler) {
        this.appointmentRepo = appointmentRepo;
        this.dailyHandler = dailyHandler;
    }


    public AppointmentResponse makeAppointment(AppointmentRequest request) throws AppointmentDateException {
        // Make appointment
        return dailyHandler.makeAppointment(request);
    }

    public AppointmentResponse cancelAppointment(AppointmentRequest request) throws AppointmentDateException {
        return dailyHandler.cancelAppointment(request);
    }

    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public AppointmentEntity getAppointmentByID(int apptID) throws AppointmentIdNotFoundException {
        Optional<AppointmentEntity> appt = appointmentRepo.findById(apptID);
        if (appt.isEmpty()) {
            throw new AppointmentIdNotFoundException("Appointment with ID: " + apptID + " is not found");
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

    public List<AppointmentEntity> getAppointmentsByDoctorUsernameBtwStartTimeAndEndTime(String username, Timestamp start_time, Timestamp end_time) {
        return appointmentRepo.findAppointmentEntitiesByDoctorUsernameAndStartDateAfterAndEndDateBefore(username, start_time, end_time);

    }

    public List<AppointmentEntity> getAppointmentsByPatientUsernameBtwStartTimeAndEndTime(String username, Timestamp start_time, Timestamp end_time) {
        start_time.setTime(start_time.getTime() - 1000L);
        end_time.setTime(end_time.getTime() + 1000L);
        return appointmentRepo.findAppointmentEntitiesByPatientUsernameAndStartDateAfterAndEndDateBefore(username, start_time, end_time);
    }



    public AppointmentResponse updateAppointment(AppointmentRequest oldRequest, AppointmentRequest newRequst)
            throws AppointmentIdNotFoundException, AppointmentDateException {

        AppointmentResponse response = dailyHandler.makeAppointment(newRequst);
        if(!response.isSuccess()) {
            return AppointmentResponse.builder()
                    .id(0)
                    .success(false)
                    .request(newRequst)
                    .build();
        }
        response = dailyHandler.cancelAppointment(oldRequest);
        if(!response.isSuccess()) {
            throw new AppointmentIdNotFoundException("No such appointment");
        }

        return AppointmentResponse.builder()
                    .id(0)
                    .success(true)
                    .request(newRequst)
                    .build();
    }




    public List<AppointmentEntity> getAppointmentsByDate(LocalDate date) {
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.plusDays(1).atStartOfDay());
        return getAppointmentsBtwStartTimeAndEndTime(startOfDay, endOfDay);
    }



    public Long getTotalAppointments() {
        return appointmentRepo.count();
    }


}
