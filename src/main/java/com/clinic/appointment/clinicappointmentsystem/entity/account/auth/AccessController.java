package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DateOfBirthMismatchException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.EmailMismatchException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AccessController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(service.authenticate(request), CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
            throws DoctorUsernameFoundException {
        return new ResponseEntity<>(service.register(request), CREATED);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<AuthenticationResponse> resetPassword(@RequestBody PasswordResetRequest request)
            throws EmailMismatchException, DateOfBirthMismatchException {
        return new ResponseEntity<>(service.resetPassword(request), CREATED);
    }
}
