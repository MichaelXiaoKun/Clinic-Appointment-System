package com.clinic.appointment.clinicappointmentsystem.entity.account;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AccountRepo accountRepo;

    public List<AccountEntity> getAccountsByFirstNameAndLastName(String firstName, String lastName) {
        List<AccountEntity> accounts = new ArrayList<>();
        if (doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName).isPresent()) {
            accounts.addAll(doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName).get());
        }
        if (patientRepo.findPatientEntitiesByFirstNameAndLastName(firstName, lastName).isPresent()) {
            accounts.addAll(patientRepo.findPatientEntitiesByFirstNameAndLastName(firstName, lastName).get());
        }
        return accounts;
    }

    public Optional<List<AccountEntity>> getDoctorsProfiles() {
        return accountRepo.findAccountEntitiesByAccountType("DOCTOR");
    }

    public Optional<List<AccountEntity>> getPatientProfiles() {
        return accountRepo.findAccountEntitiesByAccountType("PATIENT");
    }
}
