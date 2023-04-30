package com.clinic.appointment.clinicappointmentsystem.entity.appointment.Handler;


import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentEntity;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentRepo;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentRequest;
import com.clinic.appointment.clinicappointmentsystem.entity.appointment.AppointmentResponse;
import com.clinic.appointment.clinicappointmentsystem.exception.exceptionClass.AppointmentDateException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@Service
public class AppointmentHandler {

    private final AppointmentRepo appointmentRepo;

    private static int APPT_ID = 0;
    private final int interval = 15;
    private final int openAppointmentDay = 7;
    private  final int workStartHour = 8;
    private  final int workEndHour = 20;
    private int day;
    private Schedule[] schedule;

    public AppointmentHandler(AppointmentRepo appointmentRepo) throws AppointmentDateException{

        this.appointmentRepo = appointmentRepo;
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

        for (AppointmentEntity entity : entityList) {
            APPT_ID = max(APPT_ID, entity.getApptId());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ProcessedTime time = new ProcessedTime(entity.getStartDate(), entity.getEndDate());

            try {
                commonValidityCheck(time);

                boolean success = scheduleAppointment(entity);
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
        APPT_ID++;

    }

    /*
    * Interface of making an appointment
    * */

    public AppointmentResponse makeAppointment(AppointmentRequest request) throws AppointmentDateException {
        // build a entity
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

        boolean success = scheduleAppointment(entity);

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
    * Make a reservation attempt
    * The method first checks the validity of the appointment entity
    * If valid, it would attempt to make a reservation
    * Return true if succeed, false otherwise
    * */

    private boolean scheduleAppointment(AppointmentEntity entity) throws AppointmentDateException{

        // get key info
        String doctorName = entity.getDoctorUsername();
        ProcessedTime time = new ProcessedTime(entity.getStartDate(), entity.getEndDate());

        commonValidityCheck(time);
        // If doctor is on-duty
        // TODO
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
            throw new AppointmentDateException("Appointment start and end days are not in the same day");
        }
        if (time.getStartMinute() % interval != 0 || time.getEndMinute() % interval != 0) {
            throw new AppointmentDateException("Invalid appointment time slots");
        }
        if (time.getStartHour() < workStartHour || time.getStartHour() > workEndHour ||
                time.getEndHour() < workStartHour || time.getEndHour() > workEndHour) {
            throw new AppointmentDateException("Invalid appointment time. Not in working hours");
        }
        if (time.getEnd().before(time.getStart()) || time.getEnd().equals(time.getStart())) {
            throw new AppointmentDateException("Appointment end time is before or equal to appointment start time");
        }
        if (time.getStart().before(time.getCurrent())) {
            throw new AppointmentDateException("Invalid appointment time. Time expired");
        }
        long differDays = abs(ChronoUnit.DAYS.between(time.getCurrentTime(), time.getStartTime()));
        if (differDays >= 6) {
            if (differDays == 6 && (time.startHour < time.currentHour || time.startHour == time.currentHour && time.startMinute <= time.currentMinute)) {
                throw new AppointmentDateException("Invalid appointment time. Can't make appointment for a week later");
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


