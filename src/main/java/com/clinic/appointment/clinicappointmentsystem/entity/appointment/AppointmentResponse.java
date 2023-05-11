package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private int id;
    private boolean success;
    private AppointmentRequest request;
    private int appointmentID;
    private String appointmentTitle;
    private String doctorName;
    private String description;
    private String startTime;
    private String endTime;


    public AppointmentResponse(AppointmentEntity appointmentEntity) {
        this.appointmentID = appointmentEntity.getApptId();
        this.appointmentTitle = appointmentEntity.getApptTitle();
        this.doctorName = appointmentEntity.getDoctorUsername();
        this.description = appointmentEntity.getDescription();
        this.startTime = appointmentEntity.getStartDate().toString();
        this.endTime = appointmentEntity.getEndDate().toString();
    }
}
