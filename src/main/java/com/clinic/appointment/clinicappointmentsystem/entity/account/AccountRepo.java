package com.clinic.appointment.clinicappointmentsystem.entity.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity, String> {
    Optional<List<AccountEntity>> findAccountEntitiesByAccountType(String accountType);
}
