package com.clinic.appointment.clinicappointmentsystem.entity.account.config;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<DoctorEntity> doctor = doctorRepo.findById(username);

            if (doctor.isPresent()) {
                return doctor.get();
            }

            Optional<PatientEntity> patient = patientRepo.findById(username);

            return patient.orElseThrow(() -> new UsernameNotFoundException("Account not Found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
