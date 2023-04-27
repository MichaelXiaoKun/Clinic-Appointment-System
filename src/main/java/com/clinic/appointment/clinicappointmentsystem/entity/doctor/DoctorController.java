package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.domain.HttpResponse;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameNotFoundException;
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
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoctorEntity>> getAllDoctorProfiles() {
        List<DoctorEntity> allDoctors = doctorService.getAllDoctorProfiles();
        return new ResponseEntity<>(allDoctors, OK);
    }

    @GetMapping("/username={username}")
    public ResponseEntity<DoctorEntity> getDoctorByUsername(@PathVariable("username") String username)
            throws DoctorUsernameNotFoundException {
        DoctorEntity doctor = doctorService.getDoctorByUsername(username);
        return new ResponseEntity<>(doctor, OK);
    }

    @GetMapping("/firstname={firstName}_lastname={lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstNameAndLastName(
            @PathVariable("firstName") String firstName,
            @PathVariable("lastName") String lastName) {
        List<DoctorEntity> doctors = doctorService.getDoctorsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(doctors, OK);
    }

    @PostMapping
    public ResponseEntity<DoctorEntity> createDoctorAccount(@RequestBody DoctorEntity doctorEntity)
            throws DoctorUsernameFoundException {
        doctorService.createDoctor(doctorEntity);
        return new ResponseEntity<>(doctorEntity, CREATED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<HttpResponse> deleteDoctorAccount(@PathVariable("username") String username)
            throws DoctorUsernameNotFoundException {
        doctorService.deleteDoctor(username);
        return BuildResponse.build(NO_CONTENT, DOCTOR_DELETED_SUCCESSFULLY);
    }

    @PutMapping("/{username}/")
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
