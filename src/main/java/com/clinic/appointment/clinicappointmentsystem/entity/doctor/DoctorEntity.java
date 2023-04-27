package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.account.user.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "DOCTOR", schema = "CLINICADMIN")
@PrimaryKeyJoinColumn(name = "USERNAME")
public class DoctorEntity extends AccountEntity {
    @Basic
    @Column(name = "SPECIALTY", nullable = false)
    private String specialty;

    @Basic
    @Column(name = "DEGREE", nullable = false)
    private String degree;

    @Basic
    @Column(name = "LICENSE_NUMBER", nullable = false)
    private String licenseNumber;

    @Basic
    @Column(name = "BOARD_CERTIFICATION")
    private String boardCertification;

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getBoardCertification() {
        return boardCertification;
    }

    public void setBoardCertification(String boardCertification) {
        this.boardCertification = boardCertification;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    @Override
    public Date getDob() {
        return super.getDob();
    }

    @Override
    public void setDob(Date dob) {
        super.setDob(dob);
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public String getMiddleName() {
        return super.getMiddleName();
    }

    @Override
    public void setMiddleName(String middleName) {
        super.setMiddleName(middleName);
    }

    @Override
    public String getAccountType() {
        return super.getAccountType();
    }

    @Override
    public void setAccountType(String accountType) {
        super.setAccountType(accountType);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DoctorEntity doctor = (DoctorEntity) o;
        return Objects.equals(specialty, doctor.specialty) &&
                Objects.equals(degree, doctor.degree) &&
                Objects.equals(licenseNumber, doctor.licenseNumber) &&
                Objects.equals(boardCertification, doctor.boardCertification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialty, degree, licenseNumber, boardCertification);
    }
}
