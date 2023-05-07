package com.clinic.appointment.clinicappointmentsystem.entity.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNT", schema = "CLINICADMIN")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AccountEntity implements UserDetails {
    @Id
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Basic
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Basic
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "DOB", nullable = false)
    private Date dob;

    @Basic
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Basic
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Basic
    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private String accountType;

    @Basic
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Basic
    @Column(name = "IS_LOCKED", nullable = false)
    private int isLocked;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void lockAccount() {
        this.isLocked = 1;
    }

    public void unlockAccount() {
        this.isLocked = 0;
    }

    public boolean isAccountNonLocked() {
        return this.isLocked == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity account = (AccountEntity) o;
        return Objects.equals(username, account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstName, lastName, dob, phoneNumber, middleName, accountType);
    }
}
