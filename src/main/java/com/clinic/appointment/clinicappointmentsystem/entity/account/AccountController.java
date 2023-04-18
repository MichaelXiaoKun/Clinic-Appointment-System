package com.clinic.appointment.clinicappointmentsystem.entity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

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
