package com.example.amsnew.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "daily_attendance_status",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "attendance_date"})
    }
)
public class DailyAttendanceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Employee reference
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;
    
    
    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;


    public Attendance getAttendance() {
		return attendance;
	}
	public void setAttendance(Attendance attendance) {
		this.attendance = attendance;
	}
	// Attendance date
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    // PRESENT / ABSENT / WFH / PERMISSION
    @Enumerated(EnumType.STRING)
    private DailyStatus status;

    // Login / Logout
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    // Late & Overtime
    private Long lateMinutes;
    private Long overtimeMinutes;

    // Permission (minutes)
    private Long permissionMinutes;

    // Daily summary
    private Long totalWorkMinutes;
    private Long totalBreakMinutes;
    
    
    
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public DailyStatus getStatus() {
		return status;
	}
	public void setStatus(DailyStatus status) {
		this.status = status;
	}
	public LocalDateTime getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}
	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}
	public Long getLateMinutes() {
		return lateMinutes;
	}
	public void setLateMinutes(Long lateMinutes) {
		this.lateMinutes = lateMinutes;
	}
	public Long getOvertimeMinutes() {
		return overtimeMinutes;
	}
	public void setOvertimeMinutes(Long overtimeMinutes) {
		this.overtimeMinutes = overtimeMinutes;
	}
	public Long getPermissionMinutes() {
		return permissionMinutes;
	}
	public void setPermissionMinutes(Long permissionMinutes) {
		this.permissionMinutes = permissionMinutes;
	}
	public Long getTotalWorkMinutes() {
		return totalWorkMinutes;
	}
	public void setTotalWorkMinutes(Long totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
	}
	public Long getTotalBreakMinutes() {
		return totalBreakMinutes;
	}
	public void setTotalBreakMinutes(Long totalBreakMinutes) {
		this.totalBreakMinutes = totalBreakMinutes;
	}

  
    
    
    
}
