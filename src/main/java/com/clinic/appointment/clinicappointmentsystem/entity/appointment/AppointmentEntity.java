package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "APPOINTMENT", schema = "CLINICADMIN")
public class AppointmentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "APPT_ID")
    private String apptId;

    @Basic
    @Column(name = "APPT_TITLE")
    private String apptTitle;

    @Basic
    @Column(name = "START_DATE")
    private Timestamp startDate;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    @Basic
    @Column(name = "END_DATE")
    private Timestamp endDate;

    @Basic
    @Column(name = "PATIENT_USERNAME")
    private String patientUsername;

    @Basic
    @Column(name = "DOCTOR_USERNAME")
    private String doctorUsername;

    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public void setApptTitle(String apptTitle) {
        this.apptTitle = apptTitle;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentEntity that = (AppointmentEntity) o;
        return Objects.equals(apptId, that.apptId) &&
                Objects.equals(apptTitle, that.apptTitle) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(description, that.description) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(patientUsername, that.patientUsername) &&
                Objects.equals(doctorUsername, that.doctorUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apptId, apptTitle, startDate, description, endDate, patientUsername, doctorUsername);
    }
}
