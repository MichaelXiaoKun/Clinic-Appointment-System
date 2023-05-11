package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentRequest {

    AppointmentRequest oldRequest;
    AppointmentRequest newRequest;

}
