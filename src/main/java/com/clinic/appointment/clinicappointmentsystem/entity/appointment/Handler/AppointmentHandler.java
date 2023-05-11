package com.clinic.appointment.clinicappointmentsystem.entity.appointment.Handler;


import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentRequest;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentResponse;
import com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks.DoctorBreaksEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.doctorBreaks.DoctorBreaksRepo;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.max;


@Service
public class AppointmentHandler {

    private final AppointmentRepo appointmentRepo;
    private final DoctorBreaksRepo doctorBreaksRepo;

    private static int APPT_ID = 0;
    private static int BREAK_ID = 0;
    private final int interval = 15;
    private final int openAppointmentDay = 7;
    private  final int workStartHour = 8;
    private  final int workEndHour = 20;
    private Schedule[] schedule;

    private static final String NOT_SAME_DAY = "Appointment start and end days are not in the same day";
    private static final String INVALID_SLOTS = "Invalid appointment time slots";
    private static final String NOT_WORKING_HOUR = "Invalid appointment time. Not in working hours";
    private static final String END_BEFORE_START = "Appointment end time is before or equal to appointment start time";
    private static final String TIME_EXPIRED = "Invalid appointment time. Time expired";
    private static final String TIME_EXPIRED_WEEK = "Invalid appointment time. Can't make appointment for a week later";

    public AppointmentHandler(AppointmentRepo appointmentRepo, DoctorBreaksRepo doctorBreaksRepo){

        this.appointmentRepo = appointmentRepo;
        this.doctorBreaksRepo = doctorBreaksRepo;
        schedule = new Schedule[openAppointmentDay];
        for (int i = 0; i < openAppointmentDay; ++i) {
            schedule[i] = new Schedule();
        }

        Initialization();

    }

    /*
    * Initialize the memory cache based on current appointments
    * For initialization, we assume all entities already exist in the database to be valid.
    * */

    private void Initialization(){

        List<AppointmentEntity> entityList = appointmentRepo.findAll();
        List<DoctorBreaksEntity> doctorBreaks = doctorBreaksRepo.findAll();

        for (AppointmentEntity entity : entityList) {
            APPT_ID = max(APPT_ID, entity.getApptId());
            ProcessedTime time = new ProcessedTime(entity.getStartDate(), entity.getEndDate());

            try {
                commonValidityCheck(time);

                boolean success = scheduleAppointment(entity.getDoctorUsername(), entity.getStartDate(), entity.getEndDate());
                if (!success) {
                    appointmentRepo.deleteById(entity.getApptId());
                }
            }
            catch (AppointmentDateException e) {
                System.out.println("Purge invalid appointment: " + entity);
                appointmentRepo.deleteById(entity.getApptId());
                // maybe other requirements after purging
                // TODO
            }
        }

        for (DoctorBreaksEntity entity : doctorBreaks) {
            BREAK_ID = max(BREAK_ID, entity.getBreakId());
            ProcessedTime time = new ProcessedTime(entity.getStartTime(), entity.getEndTime());

            try {
                commonValidityCheck(time);

                boolean success = scheduleAppointment(entity.getDoctorUsername(), entity.getStartTime(), entity.getEndTime());
                if (!success) {
                    doctorBreaksRepo.deleteById((int) entity.getBreakId());
                }
            }
            catch (AppointmentDateException e) {
                System.out.println("Purge invalid appointment: " + entity);
                if(e.getMessage() == TIME_EXPIRED) {
                    int newMinute = (time.getCurrentMinute() % 15 + 1) * 15 % 60;
                    int newHour = time.getCurrentHour() + newMinute == 0 ? 1:0;
                    // TODO: 5/7/23
                    doctorBreaksRepo.deleteById((int) entity.getBreakId());
                }
            }
        }
        APPT_ID++;
        BREAK_ID++;
    }

    public static int getBreakId() {
        return BREAK_ID;
    }

    /*
    * Interface of making an appointment
    * */

    public AppointmentResponse makeAppointment(AppointmentRequest request) throws AppointmentDateException {
        // build a entity
        System.out.println(request);
        Timestamp startTime = Timestamp.valueOf(request.getStartTime());
        Timestamp endTime = Timestamp.valueOf(request.getEndTime());
        AppointmentEntity entity = AppointmentEntity.builder()
                .apptId(APPT_ID)
                .apptTitle(request.getAppointmentTitle())
                .description(request.getDescription())
                .doctorUsername(request.getDoctorName())
                .patientUsername(request.getPatientName())
                .startDate(startTime)
                .endDate(endTime)
                .build();

        boolean success = scheduleAppointment(entity.getDoctorUsername(), entity.getStartDate(), entity.getEndDate());

        if (success) { // if success, save the entity to the database
            APPT_ID++; // only increase by one if successful
            appointmentRepo.save(entity);
        }
        return AppointmentResponse.builder()
                .id(entity.getApptId())
                .success(success)
                .request(request)
                .build();
    }

    /*
    * Interface of canceling an appointment
    * */
    public AppointmentResponse cancelAppointment(AppointmentRequest request) throws AppointmentDateException{

        // Check if appointment exists in the database
        // This currently doesn't make use of the cache property. Can be improved
        //
        System.out.println(request);
        if (!appointmentRepo.existsById(request.getAppointmentId())) {
            return AppointmentResponse.builder()
                    .id(request.getAppointmentId())
                    .success(false)
                    .request(request)
                    .build();
        }

        AppointmentEntity entity = appointmentRepo.getReferenceById(request.getAppointmentId());

        Timestamp startTime = Timestamp.valueOf(request.getStartTime());
        Timestamp endTime = Timestamp.valueOf(request.getEndTime());

        if (!entity.getStartDate().equals(startTime) || !entity.getEndDate().equals(endTime)) {
            throw new AppointmentDateException("Time does not match with existing entity");
        }

        ProcessedTime time = new ProcessedTime(startTime, endTime);
        commonValidityCheck(time);

        boolean success = schedule[time.getStartTime().getDayOfWeek().getValue() - 1].cancelAppointment(request.getDoctorName(), time.getStartIdx(), time.getEndIdx());

        if (success) {
            System.out.printf("Cancel appointment successful: Doctor %s Appointment made from %s to %s%n",
                    request.getDoctorName(), time.getStartTime().toString(), time.getEndTime().toString());
            // delete the entity from the database
            appointmentRepo.deleteById(request.getAppointmentId());
        } else {
            System.out.printf("Cancel appointment unsuccessful: Doctor %s Appointment made from %s to %s%n",
                    request.getDoctorName(), time.getStartTime().toString(), time.getEndTime().toString());
        }

        return AppointmentResponse.builder()
                .id(request.getAppointmentId())
                .success(success)
                .request(request)
                .build();
    }

    /*
    * Make a pseudo appointment
    * The appointment will not be stored in the database,
    * but will take its place in the schedule cache
    * */

    public boolean makePseudoAppointment(String doctorName, Timestamp startTime, Timestamp endTime) throws AppointmentDateException{
        ProcessedTime time = new ProcessedTime(startTime, endTime);
        commonValidityCheck(time);

        // get all conflicted appointments. (start time before the break's end time and end time after the break's start time)
        List<AppointmentEntity> conflicts =
                appointmentRepo.findAppointmentEntitiesByDoctorUsernameAndEndDateAfterAndStartDateBefore(doctorName, startTime, endTime);

        // delete all conflicted appointments in the database and frees the cache
        for (AppointmentEntity entity : conflicts) {
            // Pseudo request
            cancelAppointment(AppointmentRequest.builder()
                    .doctorName(doctorName)
                    .appointmentId(entity.getApptId())
                    .startTime(entity.getStartDate().toLocalDateTime())
                    .endTime(entity.getEndDate().toLocalDateTime())
                    .build());
        }
        if(scheduleAppointment(doctorName, startTime, endTime)) {
            BREAK_ID++;
            return true;
        }
        return false;
    }

    public boolean cancelPseudoAppointment(String doctorName, Timestamp startTime, Timestamp endTime) throws AppointmentDateException{
        ProcessedTime time = new ProcessedTime(startTime, endTime);
        commonValidityCheck(time);

        boolean success = schedule[time.getStartTime().getDayOfWeek().getValue() - 1].cancelAppointment(doctorName, time.getStartIdx(), time.getEndIdx());

        if (success) {
            System.out.printf("Cancel pseudo appointment successful: Doctor %s Appointment made from %s to %s%n",
                    doctorName, time.getStartTime().toString(), time.getEndTime().toString());
        }else {
            System.out.printf("Cancel pseudo appointment unsuccessful: Doctor %s Appointment made from %s to %s%n",
                    doctorName, time.getStartTime().toString(), time.getEndTime().toString());
        }
        return success;
    }

    /*
    * Make a reservation attempt
    * The method first checks the validity of the appointment entity
    * If valid, it would attempt to make a reservation
    * Return true if succeed, false otherwise
    * */

    private boolean scheduleAppointment(String doctorName, Timestamp startTIme, Timestamp endTime) throws AppointmentDateException{

        // get key info
        ProcessedTime time = new ProcessedTime(startTIme, endTime);

        commonValidityCheck(time);

        // Try to make appointment
        boolean success = schedule[time.getStartTime().getDayOfWeek().getValue() - 1].makeAppointment(doctorName, time.getStartIdx(), time.getEndIdx());
        if (success) {
            System.out.printf("Make appointment successful: Doctor %s Appointment made from %s to %s%n",
                    doctorName, time.getStartTime().toString(), time.getEndTime().toString());
        } else {
            System.out.printf("Make appointment unsuccessful: Doctor %s Appointment made from %s to %s%n",
                    doctorName, time.getStartTime().toString(), time.getEndTime().toString());
        }

        return success;

    }


    private void commonValidityCheck(ProcessedTime time) throws AppointmentDateException{
        // Validity check
        if (time.getStartDay() != time.getEndDay()) {
            throw new AppointmentDateException(NOT_SAME_DAY);
        }
        if (time.getStartMinute() % interval != 0 || time.getEndMinute() % interval != 0) {
            throw new AppointmentDateException(INVALID_SLOTS);
        }
        if (time.getStartHour() < workStartHour || time.getStartHour() > workEndHour ||
                time.getEndHour() < workStartHour || time.getEndHour() > workEndHour) {
            throw new AppointmentDateException(NOT_WORKING_HOUR);
        }
        if (time.getEnd().before(time.getStart()) || time.getEnd().equals(time.getStart())) {
            throw new AppointmentDateException(END_BEFORE_START);
        }
        if (time.getStart().before(time.getCurrent())) {
            throw new AppointmentDateException(TIME_EXPIRED);
        }
        long differDays = abs(ChronoUnit.DAYS.between(time.getCurrentTime(), time.getStartTime()));
        if (differDays >= 6) {
            if (differDays == 6 && (time.startHour < time.currentHour || time.startHour == time.currentHour && time.startMinute <= time.currentMinute)) {
                throw new AppointmentDateException(TIME_EXPIRED_WEEK);
            }
        }
    }

    // Process Time
    @Data
    private class ProcessedTime {

        private Timestamp start;
        private Timestamp end;
        private Timestamp current;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LocalDateTime currentTime;
        private int startMonth;
        private int startDay;
        private int startHour;
        private int startMinute;
        private int endMonth;
        private int endDay;
        private int endHour;
        private int endMinute;
        private int currentDay;
        private int currentHour;
        private int currentMinute;
        private int startIdx;
        private int endIdx;

        public ProcessedTime (Timestamp start, Timestamp end) {
            this.start = start;
            this.end = end;
            current = new Timestamp(System.currentTimeMillis());
            startTime = start.toLocalDateTime();
            endTime = end.toLocalDateTime();
            currentTime = current.toLocalDateTime();

            startMonth = startTime.getMonthValue();
            startDay = startTime.getDayOfMonth();
            startHour = startTime.getHour();
            startMinute = startTime.getMinute();

            endMonth = endTime.getMonthValue();
            endDay = endTime.getDayOfMonth();
            endHour = endTime.getHour();
            endMinute = endTime.getMinute();

            currentDay = currentTime.getDayOfMonth();
            currentHour = currentTime.getHour();
            currentMinute = currentTime.getMinute();

            startIdx = (startHour - workStartHour) * (60 / interval) + startMinute / interval;
            endIdx = (endHour - workStartHour) * (60 / interval) + endMinute / interval - 1;
        }

    }


    // updateAppointment
    public AppointmentResponse updateAppointment(AppointmentRequest request) throws AppointmentDateException {
        // Check if appointment exists in the database
        if (!appointmentRepo.existsById(request.getAppointmentId())) {
            return AppointmentResponse.builder()
                    .id(request.getAppointmentId())
                    .success(false)
                    .request(request)
                    .build();
        }

        // Cancel the existing appointment
        AppointmentResponse cancelResponse = cancelAppointment(request);
        if (!cancelResponse.isSuccess()) {
            return AppointmentResponse.builder()
                    .id(request.getAppointmentId())
                    .success(false)
                    .request(request)
                    .build();
        }

        // Create a new appointment with updated details
        AppointmentResponse makeResponse = makeAppointment(request);
        if (!makeResponse.isSuccess()) {
            // If the updated appointment cannot be scheduled, try to revert to the original appointment
            makeAppointment(cancelResponse.getRequest());
            return AppointmentResponse.builder()
                    .id(request.getAppointmentId())
                    .success(false)
                    .request(request)
                    .build();
        }

        return AppointmentResponse.builder()
                .id(makeResponse.getId())
                .success(true)
                .request(request)
                .build();
    }




    // Get appointments for a specific date
    public List<AppointmentEntity> getAppointmentsForSpecificDate(LocalDate date) {
        List<AppointmentEntity> allAppointments = appointmentRepo.findAll();
        return allAppointments.stream()
                .filter(appointment -> appointment.getStartDate().toLocalDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }


    // Get the total number of appointments
    public long getTotalAppointments() {
        return appointmentRepo.count();
    }






    /*
    * The schedule cache for one day.
    * Maintain a doctor, appointment tree hash table for query.
    * Each doctor has a schedule for the day.
    * */

    private class Schedule {

        private HashMap<String, SegmentTree> doctorSchedule;
        private final int TotalIntervals;

        public Schedule () {
            TotalIntervals = (workEndHour - workStartHour) * (60 / interval);
            doctorSchedule = new HashMap<>();
        }

        public boolean makeAppointment(String doctorName, int startTime, int endTime) {
            if(!doctorSchedule.containsKey(doctorName)) { // if doctor does not appear in the list
                // add doctor. Might exist other approaches
                doctorSchedule.put(doctorName, new SegmentTree(TotalIntervals));
            }
            return doctorSchedule.get(doctorName).makeAppointment(startTime, endTime);
        }

        public boolean cancelAppointment(String doctorName, int startTime, int endTime) {
            if(!doctorSchedule.containsKey(doctorName)) { // if doctor does not appear in the list
                // no such doctor, return false
                return false;
            }else {
                return doctorSchedule.get(doctorName).cancelAppointment(startTime, endTime);
            }
        }

    }

}


