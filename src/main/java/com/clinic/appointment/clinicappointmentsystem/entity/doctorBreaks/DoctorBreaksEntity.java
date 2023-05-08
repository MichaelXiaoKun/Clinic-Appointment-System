package com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DOCTOR_BREAKS", schema = "CLINICADMIN")
public class DoctorBreaksEntity {
    @Basic
    @Column(name = "START_TIME")
    private Timestamp startTime;
    @Basic
    @Column(name = "END_TIME")
    private Timestamp endTime;
    @Basic
    @Column(name = "DOCTOR_USERNAME")
    private String doctorUsername;

    @Id
    @Column(name = "BREAK_ID")
    private short breakId;

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public short getBreakId() {
        return breakId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorBreaksEntity that = (DoctorBreaksEntity) o;
        return breakId == that.breakId && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(doctorUsername, that.doctorUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, doctorUsername, breakId);
    }
}
