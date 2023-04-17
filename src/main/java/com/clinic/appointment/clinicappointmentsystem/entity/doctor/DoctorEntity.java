package com.clinic.appointment.clinicappointmentsystem.entity.doctor;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "DOCTOR", schema = "CLINICADMIN")
@PrimaryKeyJoinColumn(name = "USERNAME")
public class DoctorEntity extends AccountEntity {
    @Basic
    @Column(name = "SPECIALTY")
    private String specialty;

    @Basic
    @Column(name = "DEGREE")
    private String degree;

    @Basic
    @Column(name = "LICENSE_NUMBER")
    private BigInteger licenseNumber;

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

    public BigInteger getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(BigInteger licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getBoardCertification() {
        return boardCertification;
    }

    public void setBoardCertification(String boardCertification) {
        this.boardCertification = boardCertification;
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
