package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PatientUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.utility.BuildResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/all"
     *
     * This method retrieves all patient profiles available in the system.
     * This endpoint is only accessible to doctors.
     *
     * No parameters are required for this method.
     */
    @GetMapping("/doctorView/all")
    public ResponseEntity<List<PatientEntity>> getAllPatientsProfiles() {
        List<PatientEntity> patients = patientService.getAllPatientProfiles();
        return new ResponseEntity<>(patients, OK);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/username={username}"
     *
     * This method retrieves the profile of the patient with the provided username.
     * This endpoint is only accessible to doctors.
     *
     * @param username The username of the patient whose profile is to be retrieved.
     */
    @GetMapping("/doctorView/username={username}")
    public ResponseEntity<PatientEntity> getPatientByUsername(@PathVariable("username") String username)
            throws PatientUsernameNotFoundException {
        PatientEntity patient = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(patient, OK);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/firstName={firstName}"
     *
     * This method retrieves the profiles of all patients with the provided first name.
     * This endpoint is only accessible to doctors.
     *
     * @param firstName The first name of the patients whose profiles are to be retrieved.
     */
    @GetMapping("/doctorView/firstName={firstName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByFirstName(@PathVariable("firstName") String firstName) {
        List<PatientEntity> patients = patientService.getPatientsByFirstName(firstName);
        return new ResponseEntity<>(patients, OK);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/lastName={lastName}"
     *
     * This method retrieves the profiles of all patients with the provided last name.
     * This endpoint is only accessible to doctors.
     *
     * @param lastName The last name of the patients whose profiles are to be retrieved.
     */
    @GetMapping("/doctorView/lastName={lastName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByLastName(@PathVariable("lastName") String lastName) {
        List<PatientEntity> patients = patientService.getPatientsByLastName(lastName);
        return new ResponseEntity<>(patients, OK);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/firstName={firstName}_lastName={lastName}"
     *
     * This method retrieves the profiles of all patients with the provided first and last names.
     * This endpoint is only accessible to doctors.
     *
     * @param firstName The first name of the patients whose profiles are to be retrieved.
     * @param lastName The last name of the patients whose profiles are to be retrieved.
     */
    @GetMapping("/doctorView/firstName={firstName}_lastName={lastName}")
    public ResponseEntity<List<PatientEntity>> getPatientsByFirstname(
            @PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        List<PatientEntity> patients = patientService.getPatientsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(patients, OK);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/patientView/myProfile"
     *
     * This method retrieves the profile of the patient making the request.
     * This endpoint is only accessible to the patient whose profile is being retrieved.
     *
     * The JWT token is required for this method and should be included in the Authorization header.
     */
    @GetMapping("/patientView/myProfile")
    public ResponseEntity<PatientEntity> getMyProfile(@RequestHeader("Authorization") String authHeader)
            throws PatientUsernameNotFoundException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        PatientEntity myProfile = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(myProfile, OK);
    }

    /**
     * REST API Endpoint: "POST http://localhost:9999/api/account/patient/patientView/resetpassword"
     *
     * This method allows a patient to reset their password.
     * This endpoint is only accessible to the patient whose password is being reset.
     *
     * The JWT token is required for this method and should be included in the Authorization header.
     * The new password should be included as a request parameter.
     *
     * @param password The new password for the patient.
     */
    @PostMapping("/patientView/resetpassword")
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "password") String password) {

        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        patientService.resetPassword(username, password);
        return BuildResponse.build(NO_CONTENT, PATIENT_PASSWORD_RESET_SUCCESSFULLY);
    }

    /**
     * REST API Endpoint: "DELETE http://localhost:9999/api/account/patient/patientView/myProfile"
     *
     * This method allows a patient to delete their own profile.
     * This endpoint is only accessible to the patient whose profile is being deleted.
     *
     * The JWT token is required for this method and should be included in the Authorization header.
     */
    @DeleteMapping("/patientView/myProfile")
    public ResponseEntity<HttpResponse> deletePatient(@RequestHeader("Authorization") String authHeader)
            throws PatientUsernameNotFoundException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        patientService.deletePatientByUsername(username);
        return BuildResponse.build(NO_CONTENT, PATIENT_DELETED_SUCCESSFULLY);
    }

    /**
     * REST API Endpoint: "PUT http://localhost:9999/api/account/patient/patientView/myProfile"
     *
     * This method allows a patient to update their own profile.
     * This endpoint is only accessible to the patient whose profile is being updated.
     *
     * The JWT token is required for this method and should be included in the Authorization header.
     * The parameters to be updated should be included as request parameters.
     */
    @PutMapping("/patientView/myProfile")
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

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/email"
     *
     * This method retrieves the profiles of all patients with the provided email.
     * This endpoint is only accessible to doctors.
     *
     * @param email The email of the patients whose profiles are to be retrieved.
     */
    @GetMapping("/doctorView/email")
    public List<PatientEntity> findPatientsByEmail(@RequestParam("email") String email) {
        return patientService.findPatientsByEmail(email);
    }

    /**
     * REST API Endpoint: "GET http://localhost:9999/api/account/patient/doctorView/count"
     *
     * This method retrieves the total count of patients.
     * This endpoint is only accessible to doctors.
     */
    @GetMapping("/doctorView/count")
    public long getTotalPatientsCount() {
        return patientService.getTotalPatientsCount();
    }
}
