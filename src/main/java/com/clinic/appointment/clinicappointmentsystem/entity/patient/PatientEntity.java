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
    @Column(name = "INSURANCE_PROVIDER", nullable = false)
    private String insuranceProvider;

    @Basic
    @Column(name = "POLICY_NUMBER", nullable = false)
    private String policyNumber;

    @Basic
    @Column(name = "GROUP_NUMBER")
    private String groupNumber;

    @Basic
    @Column(name = "BALANCE", nullable = false)
    private int balance;

    @Basic
    @Column(name = "INSURANCE_PHONE", nullable = false)
    private String insurancePhone;

    @Basic
    @Column(name = "EMERGENCY_FIRST_NAME", nullable = false)
    private String emergencyFirstName;

    @Basic
    @Column(name = "EMERGENCY_LAST_NAME", nullable = false)
    private String emergencyLastName;

    @Basic
    @Column(name = "EMERGENCY_PHONE_NUMBER", nullable = false)
    private String emergencyPhoneNumber;

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getInsurancePhone() {
        return insurancePhone;
    }

    public void setInsurancePhone(String insurancePhone) {
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

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
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
