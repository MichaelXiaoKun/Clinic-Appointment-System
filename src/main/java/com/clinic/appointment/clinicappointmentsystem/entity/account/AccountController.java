package com.clinic.appointment.clinicappointmentsystem.entity.account;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

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

    @GetMapping("/email")
    public ResponseEntity<List<AccountEntity>> findAccountsByEmail(@RequestParam("email") String email) {
        List<AccountEntity> accounts = accountService.getAccountsByEmail(email);
        return new ResponseEntity<>(accounts, OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<AccountEntity>> findAccountsByType(@RequestParam("type") String accountType) {
        List<AccountEntity> accounts = accountService.getAccountsByType(accountType);
        return new ResponseEntity<>(accounts, OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalAccountsCount() {
        long count = accountService.getTotalAccountsCount();
        return new ResponseEntity<>(count, OK);
    }
}
