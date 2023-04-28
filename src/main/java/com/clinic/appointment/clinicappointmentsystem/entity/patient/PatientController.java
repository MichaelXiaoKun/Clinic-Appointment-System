package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
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
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientEntity>> getAllPatientsProfiles() {
        List<PatientEntity> patients = patientService.getAllPatientProfiles();
        return new ResponseEntity<>(patients, OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<PatientEntity> getPatientByUsername(@PathVariable("username") String username)
            throws PatientUsernameNotFoundException {
        PatientEntity patient = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(patient, OK);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<PatientEntity> resetPassword(@RequestBody NewPasswordRequest request) {
        PatientEntity patientEntity = patientService.resetPassword(request.username(), request.password());
        return new ResponseEntity<>(patientEntity, ACCEPTED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<HttpResponse> deletePatient(@PathVariable("username") String username)
            throws PatientUsernameNotFoundException {
        patientService.deletePatientByUsername(username);
        return BuildResponse.build(NO_CONTENT, PATIENT_DELETED_SUCCESSFULLY);
    }

    @PutMapping("/{username}")
    public ResponseEntity<HttpResponse> updatePatient(
            @PathVariable("username") String username,
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

        patientService.updatePatient(username, firstName, lastName,
                phoneNumber, dob, middleName,
                email, insuranceProvider, policyNumber,
                groupNumber, balance, insurancePhone,
                emergencyFirstName, emergencyLastName, emergencyPhoneNumber);

        return BuildResponse.build(ACCEPTED, PATIENT_UPDATED_SUCCESSFULLY);
    }

    private record NewPasswordRequest(String username, String password) {
    }
}
