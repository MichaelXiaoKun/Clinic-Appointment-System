package com.clinic.appointment.clinicappointmentsystem.entity.account;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountEntity, String> {

}
