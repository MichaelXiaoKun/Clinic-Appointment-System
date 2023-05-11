package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks.DoctorBreaksEntity;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorBreaksOutOfRangeException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PasswordMismatchException;
import com.clinic.appointment.clinicappointmentsystem.utility.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account/doctor")
public class DoctorController {
    public static final String DOCTOR_DELETED_SUCCESSFULLY = "Doctor deleted successfully";
    public static final String DOCTOR_UPDATED_SUCCESSFULLY = "Doctor updated successfully";
    public static final String DOCTOR_PASSWORD_RESET_SUCCESSFULLY = "Doctor password reset successfully";
    public static final String BREAKS_ADDED_SUCCESSFULLY = "Breaks added successfully";
    private final DoctorService doctorService;
    private final JwtService jwtService;

    @Autowired
    public DoctorController(DoctorService doctorService,
                            JwtService jwtService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
    }

    /**
     * Fetches all doctor profiles.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/all"
     * Returns all the doctor profiles in the system.
     *
     * @return A list of all doctors in the system.
     */
    @GetMapping("/allView/all")
    public ResponseEntity<List<DoctorEntity>> getAllDoctorProfiles() {
        List<DoctorEntity> allDoctors = doctorService.getAllDoctorProfiles();
        return new ResponseEntity<>(allDoctors, OK);
    }

    /**
     * Fetches a doctor profile by username.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/username={username}"
     * Returns the doctor profile associated with the given username.
     *
     * @param username The username of the doctor.
     * @return The doctor with the specified username.
     * @throws DoctorUsernameNotFoundException if no doctor is found with the given username.
     */
    @GetMapping("/allView/username={username}")
    public ResponseEntity<DoctorEntity> getDoctorByUsername(@PathVariable("username") String username)
            throws DoctorUsernameNotFoundException {
        DoctorEntity doctor = doctorService.getDoctorByUsername(username);
        return new ResponseEntity<>(doctor, OK);
    }

    /**
     * Fetches doctor profiles by first name.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/firstname={firstName}"
     * Returns doctor profiles associated with the given first name.
     *
     * @param firstName The first name of the doctors.
     * @return A list of doctors with the specified first name.
     */
    @GetMapping("/allView/firstname={firstName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstName(@PathVariable("firstName") String firstName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByFirstName(firstName);
        return new ResponseEntity<>(doctors, OK);
    }

    /**
     * Fetches doctor profiles by last name.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/lastname={lastName}"
     * Returns doctor profiles associated with the given last name.
     *
     * @param lastName The last name of the doctors.
     * @return A list of doctors with the specified last name.
     */
    @GetMapping("/allView/lastname={lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByLastName(@PathVariable("lastName") String lastName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByLastName(lastName);
        return new ResponseEntity<>(doctors, OK);
    }


    /**
     * Fetches doctor profiles by first and last name.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/firstname={firstName}_lastname={lastName}"
     * Returns doctor profiles associated with the given first name and last name.
     *
     * @param firstName The first name of the doctors.
     * @param lastName The last name of the doctors.
     * @return A list of doctors with the specified first name and last name.
     */
    @GetMapping("/allView/firstname={firstName}_lastname={lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstNameAndLastName(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(doctors, OK);
    }

    /**
     * Fetches doctor profiles by specialty.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/specialty={specialty}"
     * Returns doctor profiles associated with the given specialty.
     *
     * @param specialty The specialty of the doctors.
     * @return A list of doctors with the specified specialty.
     */
    @GetMapping("/allView/specialty={specialty}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsBySpecialty(@PathVariable("specialty") String specialty) {
        List<DoctorEntity> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return new ResponseEntity<>(doctors, OK);
    }

    /**
     * Fetches doctor profiles by degree.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/degree={degree}"
     * Returns doctor profiles associated with the given degree.
     *
     * @param degree The degree of the doctors.
     * @return A list of doctors with the specified degree.
     */
    @GetMapping("/allView/degree={degree}")
    public ResponseEntity<List<DoctorEntity>> getDoctorByDegree(@PathVariable("degree") String degree) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByDegree(degree);
        return new ResponseEntity<>(doctors, OK);
    }

    /**
     * Fetches the logged-in doctor's profile.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/doctorView/myProfile"
     * Returns the profile of the doctor who is currently logged in.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @return The doctor's profile.
     * @throws DoctorUsernameNotFoundException if the username extracted from the JWT doesn't match any doctor.
     */
    @GetMapping("/doctorView/myProfile")
    public ResponseEntity<DoctorEntity> getMyProfile(@RequestHeader("Authorization") String authHeader)
            throws DoctorUsernameNotFoundException {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        DoctorEntity doctor = doctorService.getDoctorByUsername(username);
        return new ResponseEntity<>(doctor, OK);
    }

    /**
     * Fetches the breaks of the logged-in doctor.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/doctorView/myBreaks"
     * Returns the breaks of the doctor who is currently logged in.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @return A list of the doctor's breaks.
     */
    @GetMapping("/doctorView/myBreaks")
    public ResponseEntity<List<DoctorBreaksEntity>> getBreaks(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        List<DoctorBreaksEntity> breaksList = doctorService.getBreaksByUsername(username);
        return new ResponseEntity<>(breaksList, OK);
    }

    /**
     * Fetches the breaks of the logged-in doctor by start and end time.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/doctorView/findBreaks"
     * Returns the breaks of the doctor who is currently logged in within a specified time range.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @param startTime The start of the time range.
     * @param endTime The end of the time range.
     * @return A list of the doctor's breaks within the specified time range
     */
    @GetMapping("/doctorView/findBreaks")
    public ResponseEntity<List<DoctorBreaksEntity>> getBreaksByStartTimeAndEndTime(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "end") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        List<DoctorBreaksEntity> breaksList = doctorService.getBreaksByUsernameAndStartTimeAndEndTime(
                username, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));

        return new ResponseEntity<>(breaksList, OK);
    }

    /**
     * Changes the password of the logged-in doctor.
     * Creating a REST API: "POST http://localhost:9999/api/account/doctor/doctorView/resetpassword"
     * Changes the password of the doctor who is currently logged in.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @param password The new password.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/doctorView/resetpassword")
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "password") String password) {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        doctorService.resetPassword(username, password);
        return BuildResponse.build(NO_CONTENT, DOCTOR_PASSWORD_RESET_SUCCESSFULLY);
    }

    /**
     * Deletes the account of the logged-in doctor.
     * Creating a REST API: "DELETE http://localhost:9999/api/account/doctor/doctorView/deleteMyProfile"
     * Deletes the account of the doctor who is currently logged in.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @param password The doctor's password.
     * @return A response indicating the success of the operation.
     */
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

    /**
     * Updates the profile of the logged-in doctor.
     * Creating a REST API: "PUT http://localhost:9999/api/account/doctor/doctorView/updateMyProfile"
     * Updates the profile of the doctor who is currently logged in.
     *
     * @return A response indicating the success of the operation.
     */
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

    /**
     * Adds a break to the schedule of the logged-in doctor.
     * Creating a REST API: "POST http://localhost:9999/api/account/doctor/doctorView/addBreaks"
     * Adds a break to the schedule of the doctor who is currently logged in.
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @param startTime The start time of the break.
     * @param endTime The end time of the break.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/doctorView/addBreaks")
    public ResponseEntity<HttpResponse> addBreaks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "end") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime endTime)
            throws DoctorBreaksOutOfRangeException, AppointmentDateException {

        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        doctorService.addBreaks(username, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));

        return BuildResponse.build(ACCEPTED, BREAKS_ADDED_SUCCESSFULLY);
    }

    /**
     * Cancels a break to the schedule of the logged-in doctor.
     * Deletes a REST API: "POST http://localhost:9999/api/account/doctor/doctorView/addBreaks"
     *
     * @param authHeader The authorization header containing the JWT of the logged-in doctor.
     * @param startTime The start time of the break.
     * @param endTime The end time of the break.
     * @return A response indicating the success of the operation.
     */
    @PostMapping("/doctorView/cancelBreaks")
    public ResponseEntity<HttpResponse> cancelBreaks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "start") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "end") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime endTime)
            throws DoctorBreaksOutOfRangeException, AppointmentDateException {

        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        doctorService.cancelBreaks(username, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));

        return BuildResponse.build(ACCEPTED, BREAKS_ADDED_SUCCESSFULLY);
    }

    /**
     * Fetches doctor profiles by license number.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/licenseNumber"
     * Returns doctor profiles associated with the given license number.
     *
     * @param licenseNumber The license number of the doctors.
     * @return A list of doctors with the specified license number.
     */
    @GetMapping("/allView/licenseNumber")
    public List<DoctorEntity> getDoctorsByLicenseNumber(@RequestParam String licenseNumber) {
        return doctorService.getDoctorsByLicenseNumber(licenseNumber);
    }

    /**
     * Fetches doctor profiles by board certification.
     * Creating a REST API: "GET http://localhost:9999/api/account/doctor/allView/boardCertification"
     * Returns doctor profiles associated with the given board certification.
     *
     * @param boardCertification The board certification of the doctors.
     * @return A list of doctors with the specified board certification.
     */
    @GetMapping("/allView/boardCertification")
    public List<DoctorEntity> getDoctorsByBoardCertification(@RequestParam String boardCertification) {
        return doctorService.getDoctorsByBoardCertification(boardCertification);
    }
}
