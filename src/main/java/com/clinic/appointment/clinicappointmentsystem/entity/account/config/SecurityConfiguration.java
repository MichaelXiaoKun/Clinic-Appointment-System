package com.clinic.appointment.clinicappointmentsystem.entity.account.config;

import com.clinic.appointment.clinicappointmentsystem.entity.account.user.Role;
import com.clinic.appointment.clinicappointmentsystem.exception.JwtAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/login",
                        "/api/register",
                        "/api/forgetPassword").permitAll()
                .requestMatchers("/api/account/patient/patientView/**",
                        "/api/account/appointment/patient/patientView/**").hasAuthority(Role.PATIENT.getAuthority())
                .requestMatchers(
                        "/api/account/patient/doctorView/**",
                        "/api/account/doctor/doctorView/**").hasAuthority(Role.DOCTOR.getAuthority())
                .requestMatchers("/api/account/adminView/**").hasAuthority(Role.ADMIN.getAuthority())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");

        return http.build();
    }
}
