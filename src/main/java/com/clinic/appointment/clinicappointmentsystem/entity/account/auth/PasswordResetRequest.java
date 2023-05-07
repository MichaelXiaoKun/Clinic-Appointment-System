package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
public class PasswordResetRequest {

    private final String username;
    private final String password;
    private final String email;
    private final Date dob;
}
