package com.clinic.appointment.clinicappointmentsystem.entity.account;

import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.AuthenticationRequest;
import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.AuthenticationResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.AuthenticationService;
import com.clinic.appointment.clinicappointmentsystem.entity.account.auth.RegisterRequest;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.DoctorUsernameFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/greetings")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello! Bro, How are you?");
    }

    @GetMapping("/{firstName}_{lastName}")
    public ResponseEntity<List<AccountEntity>> getAccountsByFirstNameAndLastName(
            @PathVariable("firstName") String firstName,
            @PathVariable("lastName") String lastName) {

        List<AccountEntity> accounts = accountService.getAccountsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(accounts, OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountEntity>> getAllAccounts() {
        List<AccountEntity> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, OK);
    }

}
