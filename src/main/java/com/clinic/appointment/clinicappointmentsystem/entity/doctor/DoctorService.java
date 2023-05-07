package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.LoginAttemptService;
import com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks.DoctorBreaksEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks.DoctorBreaksRepo;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorBreaksOutOfRangeException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameNotFoundException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepo doctorRepo;
    private final LoginAttemptService loginAttemptService;
    private final PasswordEncoder passwordEncoder;
    private final DoctorBreaksRepo doctorBreaksRepo;

    @Autowired
    public DoctorService(
            DoctorRepo doctorRepo,
            LoginAttemptService loginAttemptService,
            PasswordEncoder passwordEncoder,
            DoctorBreaksRepo doctorBreaksRepo) {

        this.doctorRepo = doctorRepo;
        this.loginAttemptService = loginAttemptService;
        this.passwordEncoder = passwordEncoder;
        this.doctorBreaksRepo = doctorBreaksRepo;
    }

    public List<DoctorEntity> getAllDoctorProfiles() {
        return doctorRepo.findAll();
    }

    public DoctorEntity getDoctorByUsername(String username) throws DoctorUsernameNotFoundException {
        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found");
        }
        return doctor.get();
    }

    public List<DoctorEntity> getDoctorsByFirstNameAndLastName(String firstName, String lastName) {
        return doctorRepo.findDoctorEntitiesByFirstNameAndLastName(firstName, lastName);
    }

    public List<DoctorEntity> getDoctorsByFirstName(String firstName) {
        return doctorRepo.findDoctorEntitiesByFirstName(firstName);
    }

    public List<DoctorEntity> getDoctorsByLastName(String lastName) {
        return doctorRepo.findDoctorEntitiesByLastName(lastName);
    }

    public List<DoctorEntity> getDoctorsBySpecialty(String specialty) {
        return doctorRepo.findDoctorEntitiesBySpecialty(specialty);
    }

    public List<DoctorEntity> getDoctorsByDegree(String degree) {
        return doctorRepo.findDoctorEntitiesByDegree(degree);
    }

    public void resetPassword(String username, String new_password) {
        DoctorEntity foundDoctor = doctorRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("Doctor with username " + username + " not found")
        );

        foundDoctor.setPassword(passwordEncoder.encode(new_password));
        doctorRepo.save(foundDoctor);
    }

    private void validateLoginAttempt(DoctorEntity doctorEntity) {

        if (doctorEntity.isAccountNonLocked()) {
            if (loginAttemptService.attemptsGreaterThanAllowed(doctorEntity.getUsername())) {
//                MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO TRUE
                doctorEntity.lockAccount();
            } else {
//                MODIFY DB TO ALLOW FOR LOCK STATUS AND SET LOCK TO FALSE
                doctorEntity.unlockAccount();
            }
        } else {
            loginAttemptService.deleteUserFromLoginAttemptCache(doctorEntity.getUsername());
        }
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
                             String boardCertification) throws DoctorUsernameNotFoundException {

        Optional<DoctorEntity> doctor = doctorRepo.findById(username);
        if (doctor.isEmpty()) {
            throw new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found");
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

    public void deleteDoctor(String username, String password)
            throws DoctorUsernameNotFoundException, PasswordMismatchException {
        DoctorEntity doctor = doctorRepo.findById(username).orElseThrow(
                () -> new DoctorUsernameNotFoundException("Doctor with username: " + username + " not found")
        );

        String encoded_user_pwd = passwordEncoder.encode(password);
        if (!doctor.getPassword().equals(encoded_user_pwd)) {
            throw new PasswordMismatchException("Password is incorrect");
        }

        doctorRepo.deleteById(username);
    }

    public List<DoctorEntity> getDoctorsByLicenseNumber(String licenseNumber) {
        return doctorRepo.findDoctorEntitiesByLicenseNumber(licenseNumber);
    }

    public List<DoctorEntity> getDoctorsByBoardCertification(String boardCertification) {
        return doctorRepo.findDoctorEntitiesByBoardCertification(boardCertification);
    }

    public List<DoctorBreaksEntity> getBreaksByUsername(String username) {
        return doctorBreaksRepo.findDoctorBreaksEntitiesByDoctorUsername(username);
    }

    public List<DoctorBreaksEntity> getBreaksByUsernameAndStartTimeAndEndTime(
            String username,
            Timestamp startTime,
            Timestamp endTime) {

        return doctorBreaksRepo.findDoctorBreaksEntitiesByDoctorUsernameAndStartTimeAfterAndEndTimeBefore(
                        username, startTime, endTime);
    }

    public void addBreaks(String username, Timestamp startTime, Timestamp endTime)
            throws DoctorBreaksOutOfRangeException {

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int endHour = end.get(Calendar.HOUR_OF_DAY);

        if (startHour < 10 || startHour > 17) {
            throw new DoctorBreaksOutOfRangeException("Start time should be set to any hour between 10 AM and 5 PM");
        }
        if (endHour < 11 || endHour > 18) {
            throw new DoctorBreaksOutOfRangeException("Start time should be set to any hour between 11 AM and 6 PM");
        }

        List<DoctorBreaksEntity> breaks = doctorBreaksRepo
                .findDoctorBreaksEntitiesByDoctorUsernameAndStartTimeBefore(username, endTime);

        DoctorBreaksEntity doctorBreaks = DoctorBreaksEntity.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();

        breaks.add(doctorBreaks);

        breaks.sort(Comparator.comparing(DoctorBreaksEntity::getStartTime));

        LinkedList<DoctorBreaksEntity> mergedBreaks = new LinkedList<>();
        mergedBreaks.addLast(breaks.get(0));

        for (int id = 1; id < breaks.size(); id++) {
            DoctorBreaksEntity curr = breaks.get(id);
            DoctorBreaksEntity prev = mergedBreaks.getLast();

            if (prev.getEndTime().before(curr.getStartTime())) {
                mergedBreaks.addLast(curr);
            } else {
                prev.setEndTime(prev.getEndTime().after(curr.getEndTime()) ? prev.getEndTime() : curr.getEndTime());
                doctorBreaksRepo.delete(curr);
            }
        }

        doctorBreaksRepo.saveAll(mergedBreaks);
    }
}
