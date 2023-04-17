package com.clinic.appointment.clinicappointmentsystem.entity.patient;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountEntity;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "PATIENT", schema = "CLINICADMIN")
@PrimaryKeyJoinColumn(name = "USERNAME")
public class PatientEntity extends AccountEntity {
    @Basic
    @Column(name = "INSURANCE_PROVIDER")
    private String insuranceProvider;

    @Basic
    @Column(name = "POLICY_NUMBER")
    private BigInteger policyNumber;

    @Basic
    @Column(name = "GROUP_NUMBER")
    private BigInteger groupNumber;

    @Basic
    @Column(name = "BALANCE")
    private BigInteger balance;

    @Basic
    @Column(name = "INSURANCE_PHONE")
    private BigInteger insurancePhone;

    @Basic
    @Column(name = "EMERGENCY_FIRST_NAME")
    private String emergencyFirstName;

    @Basic
    @Column(name = "EMERGENCY_LAST_NAME")
    private String emergencyLastName;

    @Basic
    @Column(name = "EMERGENCY_PHONE_NUMBER")
    private BigInteger emergencyPhoneNumber;

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public BigInteger getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(BigInteger policyNumber) {
        this.policyNumber = policyNumber;
    }

    public BigInteger getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(BigInteger groupNumber) {
        this.groupNumber = groupNumber;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public BigInteger getInsurancePhone() {
        return insurancePhone;
    }

    public void setInsurancePhone(BigInteger insurancePhone) {
        this.insurancePhone = insurancePhone;
    }

    public String getEmergencyFirstName() {
        return emergencyFirstName;
    }

    public void setEmergencyFirstName(String emergencyFirstName) {
        this.emergencyFirstName = emergencyFirstName;
    }

    public String getEmergencyLastName() {
        return emergencyLastName;
    }

    public void setEmergencyLastName(String emergencyLastName) {
        this.emergencyLastName = emergencyLastName;
    }

    public BigInteger getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(BigInteger emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PatientEntity patient = (PatientEntity) o;
        return Objects.equals(insuranceProvider, patient.insuranceProvider) &&
                Objects.equals(policyNumber, patient.policyNumber) &&
                Objects.equals(groupNumber, patient.groupNumber) &&
                Objects.equals(balance, patient.balance) &&
                Objects.equals(insurancePhone, patient.insurancePhone) &&
                Objects.equals(emergencyFirstName, patient.emergencyFirstName) &&
                Objects.equals(emergencyLastName, patient.emergencyLastName) &&
                Objects.equals(emergencyPhoneNumber, patient.emergencyPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                insuranceProvider,
                policyNumber,
                groupNumber,
                balance,
                insurancePhone,
                emergencyFirstName,
                emergencyLastName,
                emergencyPhoneNumber
        );
    }
}
