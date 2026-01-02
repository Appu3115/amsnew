package com.example.amsnew.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_ref_id", "attendance_date"})
    }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* ================= Employee ================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_ref_id", nullable = false)
    private Employees employee;

    /* ================= Date ================= */
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    /* ================= Punch Times ================= */
    @Column(nullable = false)
    private LocalDateTime login;

    private LocalDateTime logout;

    /* ================= Shift ================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    /* ================= Metrics ================= */
    @Column(nullable = false)
    private long lateMinutes = 0;

    @Column(nullable = false)
    private long overtimeMinutes = 0;

    /* ================= Status ================= */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    /* ================= Getters & Setters ================= */

    public Integer getId() {
        return id;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getLogin() {
        return login;
    }

    public void setLogin(LocalDateTime login) {
        this.login = login;
    }

    public LocalDateTime getLogout() {
        return logout;
    }

    public void setLogout(LocalDateTime logout) {
        this.logout = logout;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public long getLateMinutes() {
        return lateMinutes;
    }

    public void setLateMinutes(long lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public long getOvertimeMinutes() {
        return overtimeMinutes;
    }

    public void setOvertimeMinutes(long overtimeMinutes) {
        this.overtimeMinutes = overtimeMinutes;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}