package com.clinic.appointment.clinicappointmentsystem.entity.account;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * Greetings message endpoint.
     * Creating a REST API: "GET http://localhost:9999/api/account/greetings"
     * Returns a greetings message.
     *
     * @return Greetings message.
     */
    @GetMapping("/adminView/greetings")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello! Bro, How are you?");
    }

    /**
     * Fetches accounts by first name and last name.
     * Creating a REST API: "GET http://localhost:9999/api/account/{firstName}_{lastName}"
     * Returns account profiles associated with the given first name and last name.
     *
     * @param firstName The first name of the account holders.
     * @param lastName The last name of the account holders.
     * @return A list of accounts with the specified first name and last name.
     */
    @GetMapping("/adminView/fullName")
    public ResponseEntity<List<AccountEntity>> getAccountsByFirstNameAndLastName(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {

        List<AccountEntity> accounts = accountService.getAccountsByFirstNameAndLastName(firstName, lastName);
        return new ResponseEntity<>(accounts, OK);
    }

    /**
     * Fetches all accounts.
     * Creating a REST API: "GET http://localhost:9999/api/account/all"
     * Returns all account profiles.
     *
     * @return A list of all accounts.
     */
    @GetMapping("/adminView/all")
    public ResponseEntity<List<AccountEntity>> getAllAccounts() {
        List<AccountEntity> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, OK);
    }

    /**
     * Fetches accounts by email.
     * Creating a REST API: "GET http://localhost:9999/api/account/email"
     * Returns account profiles associated with the given email.
     *
     * @param email The email of the account holders.
     * @return A list of accounts with the specified email.
     */
    @GetMapping("/adminView/email")
    public ResponseEntity<List<AccountEntity>> findAccountsByEmail(@RequestParam("email") String email) {
        List<AccountEntity> accounts = accountService.getAccountsByEmail(email);
        return new ResponseEntity<>(accounts, OK);
    }

    /**
     * Fetches accounts by account type.
     * Creating a REST API: "GET http://localhost:9999/api/account/type"
     * Returns account profiles associated with the given account type.
     *
     * @param accountType The type of the accounts.
     * @return A list of accounts with the specified account type.
     */
    @GetMapping("/adminView/type")
    public ResponseEntity<List<AccountEntity>> findAccountsByType(@RequestParam("type") String accountType) {
        List<AccountEntity> accounts = accountService.getAccountsByType(accountType);
        return new ResponseEntity<>(accounts, OK);
    }

    /**
     * Fetches the total number of accounts.
     * Creating a REST API: "GET http://localhost:9999/api/account/count"
     * Returns the total number of accounts.
     *
     * @return The total number of accounts.
     */
    @GetMapping("/adminView/count")
    public ResponseEntity<Long> getTotalAccountsCount() {
        long count = accountService.getTotalAccountsCount();
        return new ResponseEntity<>(count, OK);
    }

}
