package com.clinic.appointment.clinicappointmentsystem.utility;


import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BuildResponse {
    public static ResponseEntity<HttpResponse> build(HttpStatus status, String message) {
        return new ResponseEntity<>(new HttpResponse(status.value(), status,
                status.getReasonPhrase().toUpperCase(), message.toUpperCase()), status);
    }
}
