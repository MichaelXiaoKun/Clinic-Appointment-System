package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.exception.DoctorFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.DoctorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepo doctorRepo;

    @Autowired
    public DoctorService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public List<DoctorEntity> getAllDoctorProfiles() {
        return doctorRepo.findAll();
    }

    public DoctorEntity getDoctorByUsername(String username) throws DoctorNotFoundException {
        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor with username: " + username + " not found");
        }
        return doctor.get();
    }

    public List<DoctorEntity> getDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        return doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName);
    }

    public void createDoctor(DoctorEntity doctorEntity) throws DoctorFoundException {
        Optional<DoctorEntity> doctor = doctorRepo.findById(doctorEntity.getUsername());
        if (doctor.isPresent()) {
            throw new DoctorFoundException("Doctor with username: " + doctorEntity.getUsername() + " already exists");
        }
        doctorRepo.save(doctorEntity);
    }

    @Transactional
    public void updateDoctor(String username,
                             String firstName,
                             String lastName,
                             String phoneNumber,
                             Date dob,
                             String middleName,
                             String email,
                             String specialty,
                             String degree,
                             String licenseNumber,
                             String boardCertification) throws DoctorNotFoundException {

        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor with username: " + username + " not found");
        }

        DoctorEntity foundDoctor = doctor.get();

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
        doctorRepo.save(foundDoctor);
    }

    public void deleteDoctor(String username) throws DoctorNotFoundException {
        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Doctor with username: " + username + " not found");
        }
        doctorRepo.deleteById(username);
    }
}
