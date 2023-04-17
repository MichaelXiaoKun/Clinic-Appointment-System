package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    public Optional<DoctorEntity> getDoctorByUsername(String username) {
        return doctorRepo.findById(username);
    }

    public Optional<List<DoctorEntity>> getDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        return doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName);
    }

    public DoctorEntity saveDoctor(DoctorEntity doctorEntity) {
        return doctorRepo.save(doctorEntity);
    }

    public void deleteDoctor(String username) {
        doctorRepo.deleteById(username);
    }
}
