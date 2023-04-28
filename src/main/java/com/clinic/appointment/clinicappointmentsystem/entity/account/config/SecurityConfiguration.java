package com.clinic.appointment.clinicappointmentsystem.entity.account.config;

import com.clinic.appointment.clinicappointmentsystem.entity.account.user.Role;
import com.clinic.appointment.clinicappointmentsystem.exception.JwtAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class PatientSecurityConfig {

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
                    .requestMatchers("/api/login", "/api/register").permitAll()
                    .requestMatchers("/api/account/doctor/**").denyAll()
                    .requestMatchers("/api/account/patient/**").hasAuthority(Role.PATIENT.getAuthority())
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

    @Configuration
    @Order(2)
    @RequiredArgsConstructor
    public static class DoctorSecurityConfig {

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
                    .requestMatchers("/api/login", "/api/register").permitAll()
                    .requestMatchers("/api/account/patient/**").denyAll()
                    .requestMatchers("/api/account/doctor/**").hasAuthority(Role.DOCTOR.getAuthority())
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

    @Configuration
    @Order(3)
    @RequiredArgsConstructor
    public static class AdminSecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final AuthenticationEntryPoint customAuthenticationEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

        @Bean
        protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                    .csrf()
                    .disable()
                    .cors()
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/api/login", "/api/register").permitAll()
                    .requestMatchers("/api/account/**").hasAuthority(Role.ADMIN.getAuthority())
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
}
