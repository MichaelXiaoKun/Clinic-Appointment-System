package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PasswordMismatchException;
import com.clinic.appointment.clinicappointmentsystem.utility.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account/doctor")
public class DoctorController {
    public static final String DOCTOR_DELETED_SUCCESSFULLY = "Doctor deleted successfully";
    public static final String DOCTOR_UPDATED_SUCCESSFULLY = "Doctor updated successfully";
    public static final String DOCTOR_PASSWORD_RESET_SUCCESSFULLY = "Doctor password reset successfully";
    private final DoctorService doctorService;
    private final JwtService jwtService;

    @Autowired
    public DoctorController(DoctorService doctorService, JwtService jwtService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
    }


    @GetMapping("/allView/all")
    public ResponseEntity<List<DoctorEntity>> getAllDoctorProfiles() {
        List<DoctorEntity> allDoctors = doctorService.getAllDoctorProfiles();
        return new ResponseEntity<>(allDoctors, OK);
    }

    @GetMapping("/allView/username={username}")
    public ResponseEntity<DoctorEntity> getDoctorByUsername(@PathVariable("username") String username)
            throws DoctorUsernameNotFoundException {
        DoctorEntity doctor = doctorService.getDoctorByUsername(username);
        return new ResponseEntity<>(doctor, OK);
    }

    @GetMapping("/allView/firstname={firstName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstName(@PathVariable("firstName") String firstName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByFirstName(firstName);
        return new ResponseEntity<>(doctors, OK);
    }

    @GetMapping("/allView/lastname={lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByLastName(@PathVariable("lastName") String lastName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByLastName(lastName);
        return new ResponseEntity<>(doctors, OK);
    }

    @GetMapping("/allView/firstname={firstName}_lastname={lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstNameAndLastName(
            @PathVariable("firstName") String firstName,
            @PathVariable("lastName") String lastName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(doctors, OK);
    }

    @GetMapping("/allView/specialty={specialty}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsBySpecialty(@PathVariable("specialty") String specialty) {
        List<DoctorEntity> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return new ResponseEntity<>(doctors, OK);
    }

    @GetMapping("/allView/degree={degree}")
    public ResponseEntity<List<DoctorEntity>> getDoctorByDegree(@PathVariable("degree") String degree) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByDegree(degree);
        return new ResponseEntity<>(doctors, OK);
    }

    @GetMapping("/doctorView/myProfile")
    public ResponseEntity<DoctorEntity> getMyProfile(@RequestHeader("Authorization") String authHeader)
            throws DoctorUsernameNotFoundException {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        DoctorEntity doctor = doctorService.getDoctorByUsername(username);
        return new ResponseEntity<>(doctor, OK);
    }

    @PostMapping("/doctorView/resetpassword")
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "password") String password) {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        doctorService.resetPassword(username, password);
        return BuildResponse.build(NO_CONTENT, DOCTOR_PASSWORD_RESET_SUCCESSFULLY);
    }

    @DeleteMapping("/doctorView/deleteMyProfile")
    public ResponseEntity<HttpResponse> deleteDoctorAccount(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "password") String password)
            throws DoctorUsernameNotFoundException, PasswordMismatchException {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        doctorService.deleteDoctor(username, password);
        return BuildResponse.build(NO_CONTENT, DOCTOR_DELETED_SUCCESSFULLY);
    }

    @PutMapping("/doctorView/updateMyProfile")
    public ResponseEntity<HttpResponse> updateDoctorAccount(
            @PathVariable("username") String username,
            @RequestParam(value = "first_name", required = false) String firstName,
            @RequestParam(value = "last_name", required = false) String lastName,
            @RequestParam(value = "phone_number", required = false) String phoneNumber,
            @RequestParam(value = "date_of_birth", required = false) Date dob,
            @RequestParam(value = "middle_name", required = false) String middleName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "specialty", required = false) String specialty,
            @RequestParam(value = "degree", required = false) String degree,
            @RequestParam(value = "license_number", required = false) String licenseNumber,
            @RequestParam(value = "boardCertification", required = false) String boardCertification)
            throws DoctorUsernameNotFoundException {

        doctorService.updateDoctor(username,
                firstName,
                lastName,
                phoneNumber,
                dob,
                middleName,
                email,
                specialty,
                degree,
                licenseNumber,
                boardCertification);

        return BuildResponse.build(ACCEPTED, DOCTOR_UPDATED_SUCCESSFULLY);
    }
}
