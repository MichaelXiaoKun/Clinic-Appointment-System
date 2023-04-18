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
    private final AccountRepo accountRepo;

    @Autowired
    public AccountService(DoctorRepo doctorRepo, PatientRepo patientRepo, AccountRepo accountRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.accountRepo = accountRepo;
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

    public List<AccountEntity> getDoctorsProfiles() {
        return accountRepo.findAccountEntitiesByAccountType("DOCTOR");
    }

    public List<AccountEntity> getPatientProfiles() {
        return accountRepo.findAccountEntitiesByAccountType("PATIENT");
    }
}
