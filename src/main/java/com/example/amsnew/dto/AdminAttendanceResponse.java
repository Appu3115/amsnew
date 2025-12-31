package com.example.amsnew.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.amsnew.model.AttendanceStatus;
import com.example.amsnew.model.DailyStatus;

public class AdminAttendanceResponse {

	// Employee info
    private String employeeId;
    private String employeeName;

    // Date
    private LocalDate attendanceDate;

    // Attendance table
    private LocalDateTime login;
    private LocalDateTime logout;
    private long lateMinutes;
    private long overtimeMinutes;
    private AttendanceStatus attendanceStatus;

    // DailyAttendanceStatus table
    private DailyStatus dailyStatus;
    private Long totalWorkMinutes;
    private Long totalBreakMinutes;
    private Long permissionMinutes;
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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
	public AttendanceStatus getAttendanceStatus() {
		return attendanceStatus;
	}
	public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
	public DailyStatus getDailyStatus() {
		return dailyStatus;
	}
	public void setDailyStatus(DailyStatus dailyStatus) {
		this.dailyStatus = dailyStatus;
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
	public Long getPermissionMinutes() {
		return permissionMinutes;
	}
	public void setPermissionMinutes(Long permissionMinutes) {
		this.permissionMinutes = permissionMinutes;
	}
}
