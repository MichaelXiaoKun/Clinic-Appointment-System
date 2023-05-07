package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.entity.account.user.Role;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientRepo;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DateOfBirthMismatchException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.EmailMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws DoctorUsernameFoundException {
        System.out.println(request.getAccountType());
        String jwtToken = "";
        AccountEntity user = null;
        if (request.getAccountType().equalsIgnoreCase("DOCTOR")) {
            Optional<DoctorEntity> doctor = doctorRepo.findById(request.getUsername());
            if (doctor.isPresent()) {
                throw new DoctorUsernameFoundException("Account with Username " + request.getUsername() + " already exists");
            }
            user = DoctorEntity.builder()
                    // Doctor Info
                    .specialty(request.getSpecialty())
                    .degree(request.getDegree())
                    .licenseNumber(request.getLicenseNumber())
                    .boardCertification(request.getBoardCertification())
                    .role(Role.DOCTOR)
                    // Profile Info
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dob(request.getDob())
                    .phoneNumber(request.getPhoneNumber())
                    .middleName(request.getMiddleName())
                    .accountType("DOCTOR")
                    .email(request.getEmail())
                    .isLocked(0)
                    .build();

            doctorRepo.save((DoctorEntity) user);
            jwtToken = jwtService.generateToken((DoctorEntity) user);
        }

        if (request.getAccountType().equalsIgnoreCase("PATIENT")) {
            user = PatientEntity.builder()
                    // Patient Info
                    .insuranceProvider(request.getInsuranceProvider())
                    .policyNumber(request.getPolicyNumber())
                    .groupNumber(request.getGroupNumber())
                    .balance(request.getBalance())
                    .insurancePhone(request.getInsurancePhone())
                    .emergencyFirstName(request.getEmergencyFirstName())
                    .emergencyLastName(request.getEmergencyLastName())
                    .emergencyPhoneNumber(request.getEmergencyPhoneNumber())
                    .role(Role.PATIENT)
                    // Profile Info
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .dob(request.getDob())
                    .phoneNumber(request.getPhoneNumber())
                    .middleName(request.getMiddleName())
                    .accountType("PATIENT")
                    .email(request.getEmail())
                    .build();

            patientRepo.save((PatientEntity) user);
            jwtToken = jwtService.generateToken((PatientEntity) user);
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse resetPassword(PasswordResetRequest request)
            throws DateOfBirthMismatchException, EmailMismatchException {

        Optional<DoctorEntity> doctorUser = doctorRepo.findById(request.getUsername());
        Optional<PatientEntity> patientUser = patientRepo.findById(request.getUsername());

        if (doctorUser.isEmpty() && patientUser.isEmpty()) {
            throw new UsernameNotFoundException("Username or Password is incorrect");
        }

        AccountEntity user = null;
        if (doctorUser.isPresent()) {
            user = doctorUser.get();
        } else {
            user = patientUser.get();
        }

        if (!user.getDob().equals(request.getDob())) {
            throw new DateOfBirthMismatchException("Date of Birth is not in the record with username "
                    + request.getUsername());
        }
        if (!user.getEmail().equals(request.getEmail())) {
            throw new EmailMismatchException("Email is not in the record with username " + request.getUsername());
        }

        user.setPassword(request.getPassword());

        String jwtToken = jwtService.generateToken(
                user.getAccountType()
                        .equalsIgnoreCase("DOCTOR") ? (DoctorEntity) user : (PatientEntity) user
        );

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Optional<DoctorEntity> doctorUser = doctorRepo.findById(request.getUsername());
        Optional<PatientEntity> patientUser = patientRepo.findById(request.getUsername());

        AccountEntity user = null;
        if (doctorUser.isPresent()) {
            user = doctorUser.get();
        } else if (patientUser.isPresent()) {
            user = patientUser.get();
        }

        String jwtToken = jwtService.generateToken(
                user.getAccountType()
                        .equalsIgnoreCase("DOCTOR") ? (DoctorEntity) user : (PatientEntity) user
        );

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
