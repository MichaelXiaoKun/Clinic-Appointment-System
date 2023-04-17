package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/{username}")
    public ResponseEntity<DoctorEntity> getDoctorByUsername(@PathVariable("username") String username) {
        Optional<DoctorEntity> doctor = doctorService.getDoctorByUsername(username);
        if (!doctor.isPresent()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(doctor.get(), OK);
    }

    @GetMapping("/{firstName}_{lastName}")
    public ResponseEntity<List<DoctorEntity>> getDoctorsByFirstNameAndLastName(
            @PathVariable("firstName") String firstName,
            @PathVariable("lastName") String lastName) {

        Optional<List<DoctorEntity>> doctors = doctorService.getDoctorsByFirstNameAndLastName(firstName, lastName);

        if (doctors.isEmpty() || doctors.get().size() == 0) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(doctors.get(), OK);
    }

    @PostMapping
    public ResponseEntity<DoctorEntity> createDoctorAccount(@RequestBody DoctorEntity doctorEntity) {
        Optional<DoctorEntity> storingDoctorAccount = doctorService.getDoctorByUsername(doctorEntity.getUsername());

        if (storingDoctorAccount.isPresent()) {
            return new ResponseEntity<>(FOUND);
        }

        if (!doctorEntity.getAccountType().equals("DOCTOR")) {

            return new ResponseEntity<>(BAD_REQUEST);
        }

        return new ResponseEntity<>(doctorService.saveDoctor(doctorEntity), CREATED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteDoctorAccount(@PathVariable("username") String username) {
        if (doctorService.getDoctorByUsername(username).isPresent()) {
            doctorService.deleteDoctor(username);
        }

        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping("/{username}/{}")
    public ResponseEntity<DoctorEntity> updateDoctorAccount(
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
            @RequestParam(value = "boardCertification", required = false) String boardCertification) {

        Optional<DoctorEntity> selectedDoctor = doctorService.getDoctorByUsername(username);

        if (selectedDoctor.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        DoctorEntity foundDoctor = selectedDoctor.get();

        if (firstName != null) {
            foundDoctor.setFirstName(firstName);
        }
        if (lastName != null) {
            foundDoctor.setLastName(lastName);
        }
        if (phoneNumber != null) {
            foundDoctor.setPhoneNumber(phoneNumber);
        }
        if (dob != null) {
            foundDoctor.setDob(dob);
        }
        if (middleName != null) {
            foundDoctor.setMiddleName(middleName);
        }
        if (email != null) {
            foundDoctor.setEmail(email);
        }
        if (specialty != null) {
            foundDoctor.setSpecialty(specialty);
        }
        if (degree != null) {
            foundDoctor.setDegree(degree);
        }
        if (licenseNumber != null) {
            foundDoctor.setLicenseNumber(licenseNumber);
        }
        if (boardCertification != null) {
            foundDoctor.setBoardCertification(boardCertification);
        }

        return new ResponseEntity<>(foundDoctor, OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountEntity>> getAllDoctors() {
        Optional<List<AccountEntity>> allDoctors = accountService.getDoctorsProfiles();

        if (allDoctors.isEmpty() || allDoctors.get().size() == 0) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(allDoctors.get(), OK);
    }
}
