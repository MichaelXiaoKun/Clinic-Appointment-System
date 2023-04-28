package com.clinic.appointment.clinicappointmentsystem.entity.account.user;

public enum Role {

    PATIENT("ROLE_PATIENT"),
    DOCTOR("ROLE_DOCTOR"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
