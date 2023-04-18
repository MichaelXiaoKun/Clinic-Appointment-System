package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountService;
import com.clinic.appointment.clinicappointmentsystem.exception.PatientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/{username}")
    public ResponseEntity<PatientEntity> getPatientByUsername(@PathVariable("username") String username)
            throws PatientNotFoundException {
        PatientEntity patient = patientService.getPatientByUsername(username);
        return new ResponseEntity<>(patient, OK);
    }

    @PostMapping
    public ResponseEntity<Void> createPatient(@RequestBody PatientEntity patientEntity)
            throws PatientNotFoundException {
        PatientEntity possiblePatient = patientService.getPatientByUsername(patientEntity.getUsername());
        //TODO: Need to add return value
        return null;
    }
}
