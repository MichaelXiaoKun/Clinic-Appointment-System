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
import java.time.LocalDateTime;
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
     Creating a REST API: "POST http://localhost:9999/api/account/appointment/patient/patientView/make"
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

    /**
     * Creating a REST API: "POST http://localhost:9999/api/account/appointment/patient/patientView/make"
     * Schedule an appointment for logged-in patients
     * Passing in JSON (date should be in "yyyy-MM-dd HH:mm:ss"):
     * {
     *     "appointmentTitle": ...,
     *     "doctorName": ...,
     *     "description": ...,
     *     "startTime": ...,
     *     "endTime": ...
     * }
     *
     * In Postman, select POST as the method, input the URL, and set the request body to raw JSON.
     * Authorization header should be set with a valid JWT token for a patient account.
     */
    @PostMapping("/patient/patientView/make")
    public synchronized ResponseEntity<AppointmentResponse> makeAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.makeAppointment(request));
    }


    /**
     * Creating a REST API: "DELETE http://localhost:9999/api/account/appointment/patient/patientView/cancel/{appointmentId}"
     * Cancel an existing appointment for logged-in patients
     * An appointmentId needs to be passed in the URL as a path variable.
     *
     * In Postman, select DELETE as the method, input the URL, replacing {appointmentId} with a valid appointment ID.
     * Authorization header should be set with a valid JWT token for a patient account.
     */
    @DeleteMapping("/patient/patientView/cancel")
    public synchronized ResponseEntity<AppointmentResponse> cancelAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody AppointmentRequest request) throws AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        request.setPatientName(username);
        return ResponseEntity.ok(appointmentService.cancelAppointment(request));
    }


    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/allView/myAppointments"
     * Get all appointments for the logged-in user (either patient or doctor).
     *
     * In Postman, select GET as the method, input the URL.
     * Authorization header should be set with a valid JWT token for a patient or a doctor account.
     */
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


    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/doctor/doctorView/{apptID}"
     * Get specific appointment details by appointment ID.
     * An appointmentId needs to be passed in the URL as a path variable.
     *
     * In Postman, select GET as the method, input the URL, replacing {apptID} with a valid appointment ID.
     * Authorization header should be set with a valid JWT token for a doctor account.
     */
    @GetMapping("/doctor/doctorView/{apptID}")
    public ResponseEntity<AppointmentEntity> getAppointmentByID(@PathVariable("apptID") int apptID)
            throws AppointmentIdNotFoundException {
        return new ResponseEntity<>(appointmentService.getAppointmentByID(apptID), OK);
    }


    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/doctor/doctorView/{doctor_username}"
     * Get all appointments associated with a specific doctor's username.
     * A doctor's username needs to be passed in the URL as a path variable.
     *
     * In Postman, select GET as the method, input the URL, replacing {doctor_username} with a valid doctor's username.
     * Authorization header should be set with a valid JWT token for a doctor account.
     */
    @GetMapping("/doctor/doctorView/{doctor_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDoctorUsername(@PathVariable("doctor_username") String doctor_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDoctorUsername(doctor_username), OK);
    }


    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/patient/doctorView/{patient_username}"
     * Get all appointments associated with a specific patient's username.
     * A patient's username needs to be passed in the URL as a path variable.
     *
     * In Postman, select GET as the method, input the URL, replacing {patient_username} with a valid patient's username.
     * Authorization header should be set with a valid JWT token for a patient account.
     */
    @GetMapping("/patient/doctorView/{patient_username}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByPatientUsername(@PathVariable("patient_username") String patient_username) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByPatientUsername(patient_username), OK);
    }



    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/allView/{start_time}_{end_time}"
     * Get appointments for the logged-in user (either patient or doctor) between a specific start and end time.
     * The start_time and end_time (format: yyyy-MM-dd HH:mm:ss) need to be passed in the URL as path variables.
     *
     * In Postman, select GET as the method, input the URL, replacing {start_time} and {end_time} with valid timestamps.
     * Authorization header should be set with a valid JWT token for a patient or a doctor account.
     */
    @GetMapping("/allView/{start_time}_{end_time}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsBtwStartTimeAndEndDateTime(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("start_time") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime st,
            @RequestParam("end_time") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime et) {

        Timestamp start_time = Timestamp.valueOf(st);
        Timestamp end_time = Timestamp.valueOf(et);
        System.out.println(start_time);
        System.out.println(end_time);

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


    /**
     * Creating a REST API: "PUT http://localhost:9999/api/account/appointment/patient/patientView/update"
     * Update an existing appointment for logged-in patients
     * Passing in JSON (date should be in "yyyy-MM-dd HH:mm:ss"):
     * {
     *     "appointmentId": ...,
     *     "appointmentTitle": ...,
     *     "doctorName": ...,
     *     "description": ...,
     *     "startTime": ...,
     *     "endTime": ...
     * }
     * 
     * This method will update the appointment's title, doctor's name, description,
     * start time and end time according to the provided appointment ID.
     * Note: The appointment's ID and the user's username cannot be changed.
     *
     * In Postman, select PUT as the method, input the URL, and set the request body to raw JSON.
     * Authorization header should be set with a valid JWT token for a patient account.
     */
    @PutMapping("/patient/patientView/update")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AppointmentRequest oldRequest,
            @RequestBody AppointmentRequest newRequest
    ) throws AppointmentIdNotFoundException, AppointmentDateException {
        String jwtToken = authHeader.substring(7);
        String username = this.jwtService.extractUsername(jwtToken);
        oldRequest.setPatientName(username);
        newRequest.setPatientName(username);
        return ResponseEntity.ok(appointmentService.updateAppointment(oldRequest, newRequest));
    }




    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/doctor/doctorView/date/{date}"
     * Get appointments for a specific date
     * A date needs to be passed in the URL as a path variable (date should be in "yyyy-MM-dd").
     *
     * In Postman, select GET as the method, input the URL, replacing {date} with a valid date.
     * Authorization header should be set with a valid JWT token for a doctor account.
     */
    @GetMapping("/doctor/doctorView/date/{date}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentsByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByDate(date), OK);
    }


    /**
     * Creating a REST API: "GET http://localhost:9999/api/account/appointment/doctor/doctorView/count"
     * Get total number of appointments for the logged-in patient.
     *
     * In Postman, select GET as the method, input the URL.
     * Authorization header should be set with a valid JWT token for a doctor account.
     */
    @GetMapping("/doctor/doctorView/count")
    public ResponseEntity<Long> getTotalAppointments() {
        return new ResponseEntity<>(appointmentService.getTotalAppointments(), OK);
    }
}
