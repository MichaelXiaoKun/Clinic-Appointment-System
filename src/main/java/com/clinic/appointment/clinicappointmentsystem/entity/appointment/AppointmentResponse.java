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

}
