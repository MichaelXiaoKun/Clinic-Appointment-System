package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorService;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final JwtService jwtService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService,
                                 JwtService jwtService) {

        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.jwtService = jwtService;
    }

     /***
     Creating a REST API: "POST http://localhost:8080/api/account/appointment/patient/patientView/make"
     Schedule an appointment for logged-in patients
     Passing in JSON (date should be in "yyyy-MM-dd HH:mm:ss"):
      {
          "appointmentTitle": ...,
          "patientName": ...,
          "doctorName": ...,
          "description": ...,
          "startTime": ...,
          "endTime": ...
      }
     */
    @PostMapping("/patient/patientView/make")
    public synchronized ResponseEntity<AppointmentResponse> makeAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.makeAppointment(request));
    }

    @DeleteMapping("/patient/patientView/cancel")
    public synchronized ResponseEntity<AppointmentResponse> cancelAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.cancelAppointment(request));
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

    @PutMapping("/update")
    public ResponseEntity<AppointmentResponse> updateAppointment(@RequestBody AppointmentRequest request) throws AppointmentDateException, AppointmentIdNotFoundException {
        return ResponseEntity.ok(appointmentService.updateAppointment(request));
    }
    // Update an appointment

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDate(date), OK);
    }
    // Get appointments for a specific date

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalAppointments() {
        return new ResponseEntity<>(appointmentService.getTotalAppointments(), OK);
    }
    // Get the total number of appointments
}
