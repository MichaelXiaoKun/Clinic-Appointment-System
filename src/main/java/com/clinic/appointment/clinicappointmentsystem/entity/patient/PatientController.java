package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PatientUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.utility.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import statements
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account/patient")
public class PatientController {
    public static final String PATIENT_DELETED_SUCCESSFULLY = "Patient deleted successfully";
    public static final String PATIENT_UPDATED_SUCCESSFULLY = "Patient updated successfully";
    public static final String PATIENT_PASSWORD_RESET_SUCCESSFULLY = "Patient password reset successfully";
    private final PatientService patientService;
    private final JwtService jwtService;

    @Autowired
    public PatientController(PatientService patientService, JwtService jwtService) {
        this.patientService = patientService;
        this.jwtService = jwtService;
    }

    @GetMapping("/doctorView/all")
    public ResponseEntity<List<PatientEntity>> getAllPatientsProfiles() {
        List<PatientEntity> patients = patientService.getAllPatientProfiles();
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/doctorView/username={username}")
    public ResponseEntity<PatientEntity> getPatientByUsername(@PathVariable("username") String username)
            throws PatientUsernameNotFoundException {
        PatientEntity patient = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(patient, OK);
    }

    @GetMapping("/doctorView/firstName={firstName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByFirstName(@PathVariable("firstName") String firstName) {
        List<PatientEntity> patients = patientService.getPatientsByFirstName(firstName);
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/doctorView/lastName={lastName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByLastName(@PathVariable("lastName") String lastName) {
        List<PatientEntity> patients = patientService.getPatientsByLastName(lastName);
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/doctorView/firstName={firstName}_lastName={lastName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByFirstname(
            @PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        List<PatientEntity> patients = patientService.getPatientsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/patientView/myProfile")
    public ResponseEntity<PatientEntity> getMyProfile(@RequestHeader("Authorization") String authHeader)
            throws PatientUsernameNotFoundException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        PatientEntity myProfile = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(myProfile, OK);
    }

    @PostMapping("/patientView/resetpassword")
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "password") String password) {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        patientService.resetPassword(username, password);
        return BuildResponse.build(NO_CONTENT, PATIENT_PASSWORD_RESET_SUCCESSFULLY);
    }

    @DeleteMapping("/patientView/myProfile")
    public ResponseEntity<HttpResponse> deletePatient(@RequestHeader("Authorization") String authHeader)
            throws PatientUsernameNotFoundException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        patientService.deletePatientByUsername(username);
        return BuildResponse.build(NO_CONTENT, PATIENT_DELETED_SUCCESSFULLY);
    }

    @PutMapping("patientView/myProfile")
    public ResponseEntity<HttpResponse> updatePatient(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "first_name", required = false) String firstName,
            @RequestParam(name = "last_name", required = false) String lastName,
            @RequestParam(name = "phone_number", required = false) String phoneNumber,
            @RequestParam(name = "date_of_birth", required = false) Date dob,
            @RequestParam(name = "middle_name", required = false) String middleName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "insurance_provider", required = false) String insuranceProvider,
            @RequestParam(name = "policy_number", required = false) String policyNumber,
            @RequestParam(name = "group_number", required = false) String groupNumber,
            @RequestParam(name = "balance", required = false) Integer balance,
            @RequestParam(name = "insurancePhone", required = false) String insurancePhone,
            @RequestParam(name = "emergencyFirstName", required = false) String emergencyFirstName,
            @RequestParam(name = "emergencyLastName", required = false) String emergencyLastName,
            @RequestParam(name = "emergencyPhoneNumber", required = false) String emergencyPhoneNumber)
            throws PatientUsernameNotFoundException {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);

        patientService.updatePatient(username, firstName, lastName,
                phoneNumber, dob, middleName,
                email, insuranceProvider, policyNumber,
                groupNumber, balance, insurancePhone,
                emergencyFirstName, emergencyLastName, emergencyPhoneNumber);

        return BuildResponse.build(ACCEPTED, PATIENT_UPDATED_SUCCESSFULLY);
    }

    @GetMapping("/api/patients/email")
    public List<PatientEntity> findPatientsByEmail(@RequestParam("email") String email) {
        return patientService.findPatientsByEmail(email);
    }

    @GetMapping("/api/patients/count")
    public long getTotalPatientsCount() {
        return patientService.getTotalPatientsCount();
    }
}
