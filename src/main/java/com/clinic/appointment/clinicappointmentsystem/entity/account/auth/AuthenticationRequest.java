package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationRequest {

    private final String password;
    private final String username;
}
