package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

import com.clinic.appointment.clinicappointmentsystem.entity.account.AccountService;
import com.clinic.appointment.clinicappointmentsystem.entity.account.config.JwtService;
import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorService;
import com.clinic.appointment.clinicappointmentsystem.entity.patient.PatientService;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AccountService accountService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final JwtService jwtService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 AccountService accountService, DoctorService doctorService,
                                 PatientService patientService,
                                 JwtService jwtService) {

        this.appointmentService = appointmentService;
        this.accountService = accountService;
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
    public ResponseEntity<AppointmentResponse> makeAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.makeAppointment(request));
    }

    @DeleteMapping("/patient/patientView/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.cancelAppointment(request));
    }

    @GetMapping("/allView/myAppointments")
    public ResponseEntity<List<AppointmentEntity>> getAllAppointments(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        String type = accountService.getAccountTypeByUsername(username);

        List<AppointmentEntity> appts;
        if (type.equals("PATIENT")) {
            appts = appointmentService.getAppointmentsByPatientUsername(username);
        } else {
            appts = appointmentService.getAppointmentsByDoctorUsername(username);
        }

        return new ResponseEntity<>(appts, OK);
    }

    @GetMapping("/doctor/doctorView/{apptID}")
    public ResponseEntity<AppointmentEntity> getAppointmentByID(@PathVariable("apptID") int apptID)
            throws AppointmentIdNotFoundException {
        return new ResponseEntity<>(appointmentService.getAppointmentByID(apptID), OK);
    }

    @GetMapping("/doctor/doctorView/{doctor_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDoctorUsername(@PathVariable("doctor_username") String doctor_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctorUsername(doctor_username), OK);
    }

    @GetMapping("/patient/doctorView/{patient_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByPatientUsername(@PathVariable("patient_username") String patient_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByPatientUsername(patient_username), OK);
    }

    @GetMapping("/allView/{start_time}_{end_time}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsBtwStartTimeAndEndDateTime(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("start_time") Timestamp start_time,
            @PathVariable("end_time") Timestamp end_time) {

        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        String type = accountService.getAccountTypeByUsername(username);

        List<AppointmentEntity> appts;
        if (type.equals("PATIENT")) {
            appts = appointmentService.getAppointmentsByPatientUsernameBtwStartTimeAndEndTime(username, start_time, end_time);
        } else {
            appts = appointmentService.getAppointmentsByDoctorUsernameBtwStartTimeAndEndTime(username, start_time, end_time);
        }
        return new ResponseEntity<>(appts, OK);
    }

    @PutMapping("/allView/update")
    public ResponseEntity<AppointmentResponse> updateAppointment(@RequestBody AppointmentRequest request) throws AppointmentDateException, AppointmentIdNotFoundException {
        return ResponseEntity.ok(appointmentService.updateAppointment(request));
    }
    // Update an appointment

    // Get appointments for a specific date
    /*** Get appointments for a specific date in 'YYYY-MM-DD' */
    @GetMapping("/allView/date/{date}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDate(date), OK);
    }

    /*** Get the total number of appointments */
    @GetMapping("/allView/count")
    public ResponseEntity<Long> getTotalAppointments() {
        return new ResponseEntity<>(appointmentService.getTotalAppointments(), OK);
    }
}
