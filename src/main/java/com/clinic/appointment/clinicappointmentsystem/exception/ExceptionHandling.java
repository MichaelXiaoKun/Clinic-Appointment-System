package com.clinic.appointment.clinicappointmentsystem.exception;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ExceptionHandling {

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(
                httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase()), httpStatus);
    }

    @ExceptionHandler(AppointmentIdNotFoundException.class)
    public ResponseEntity<HttpResponse> appointmentIdNotFoundException(AppointmentIdNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DoctorUsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> doctorUsernameNotFoundException(DoctorUsernameNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DoctorUsernameFoundException.class)
    public ResponseEntity<HttpResponse> doctorUsernameFoundException(DoctorUsernameFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PatientUsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> patientUsernameNotFoundException(PatientUsernameNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PatientUsernameFoundException.class)
    public ResponseEntity<HttpResponse> patientUsernameFoundException(PatientUsernameFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DateOfBirthMismatchException.class)
    public ResponseEntity<HttpResponse> expiredJwtException(DateOfBirthMismatchException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<HttpResponse> expiredJwtException(PasswordMismatchException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailMismatchException.class)
    public ResponseEntity<HttpResponse> expiredJwtException(EmailMismatchException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }
}
