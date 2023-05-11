package com.clinic.appointment.clinicappointmentsystem.entity.appointment;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    private int appointmentId;
    private String appointmentTitle;
    private String patientName;
    private String doctorName;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setApptId(int appointmentID) {
        this.appointmentId = appointmentID;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorUsername(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalDateTime.parse(endTime);
    }
}


