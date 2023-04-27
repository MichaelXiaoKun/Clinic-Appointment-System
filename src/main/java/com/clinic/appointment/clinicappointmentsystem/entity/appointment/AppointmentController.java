package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorService;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService) {

        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentEntity>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), OK);
    }

    @GetMapping("/{apptID}")
    public ResponseEntity<AppointmentEntity> getAppointmentByID(@PathVariable("apptID") int apptID)
            throws AppointmentIdNotFoundException {
        return new ResponseEntity<>(appointmentService.getAppointmentByID(apptID), OK);
    }

    @GetMapping("/doctor/{doctor_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDoctorUsername(@PathVariable("doctor_username") String doctor_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctorUsername(doctor_username), OK);
    }

    @GetMapping("/patient/{patient_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByPatientUsername(@PathVariable("patient_username") String patient_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByPatientUsername(patient_username), OK);
    }

    @GetMapping("/{start_time}_{end_time}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsBtwStartTimeAndEndDateTime(
            @PathVariable("start_time") Timestamp start_time,
            @PathVariable("end_time") Timestamp end_time) {
        List<AppointmentEntity> appts = appointmentService.getAppointmentsBtwStartTimeAndEndTime(start_time, end_time);
        return new ResponseEntity<>(appts, OK);
    }
}
