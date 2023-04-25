package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date dob;
    private String phoneNumber;
    private String middleName;
    private String accountType;
    private String email;

    // Doctor Info
    private String specialty;
    private String degree;
    private String licenseNumber;
    private String boardCertification;

    // Patient Info
    private String insuranceProvider;
    private String policyNumber;
    private String groupNumber;
    private int balance;
    private String insurancePhone;
    private String emergencyFirstName;
    private String emergencyLastName;
    private String emergencyPhoneNumber;
}
