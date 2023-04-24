package com.clinic.appointment.clinicappointmentsystem.entity.account.config;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.security.auth.login.AccountNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AccountRepo accountRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> accountRepo.findById(username).orElseThrow(
                    () -> new UsernameNotFoundException("Account not found")
               );
    }
}
