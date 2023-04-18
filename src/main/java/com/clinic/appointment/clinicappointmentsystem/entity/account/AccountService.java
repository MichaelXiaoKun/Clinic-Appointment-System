package com.clinic.appointment.clinicappointmentsystem.entity.account;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    @Autowired
    public AccountService(DoctorRepo doctorRepo, PatientRepo patientRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    public List<AccountEntity> getAccountsByFirstNameAndLastName(String firstName, String lastName) {
        List<AccountEntity> accounts = new ArrayList<>();
        if (doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName) != null) {
            accounts.addAll(doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName));
        }
        if (patientRepo.findPatientEntitiesByFirstNameAndLastName(firstName, lastName) != null) {
            accounts.addAll(patientRepo.findPatientEntitiesByFirstNameAndLastName(firstName, lastName));
        }
        return accounts;
    }

    public List<AccountEntity> getAllAccounts() {
        List<AccountEntity> accounts = new ArrayList<>();

        accounts.addAll(doctorRepo.findAll());
        accounts.addAll(patientRepo.findAll());

        return accounts;
    }
}
