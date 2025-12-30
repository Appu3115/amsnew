package com.example.amsnew.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.amsnew.model.DailyStatus;

public class AdminAttendanceDTO {

    private String employeeId;
    private String employeeName;

    private LocalDate attendanceDate;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    private DailyStatus status;

    private Long lateMinutes;
    private Long overtimeMinutes;
    private Long totalWorkMinutes;
    private Long totalBreakMinutes;
    private Long permissionMinutes;

    public AdminAttendanceDTO(
            String employeeId,
            String employeeName,
            LocalDate attendanceDate,
            LocalDateTime loginTime,
            LocalDateTime logoutTime,
            DailyStatus status,
            Long lateMinutes,
            Long overtimeMinutes,
            Long totalWorkMinutes,
            Long totalBreakMinutes,
            Long permissionMinutes
    ) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.attendanceDate = attendanceDate;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.status = status;
        this.lateMinutes = lateMinutes;
        this.overtimeMinutes = overtimeMinutes;
        this.totalWorkMinutes = totalWorkMinutes;
        this.totalBreakMinutes = totalBreakMinutes;
        this.permissionMinutes = permissionMinutes;
    }

	public String getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}



	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}



	public LocalDateTime getLoginTime() {
		return loginTime;
	}


	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public DailyStatus getStatus() {
		return status;
	}

	public Long getLateMinutes() {
		return lateMinutes;
	}


	public Long getOvertimeMinutes() {
		return overtimeMinutes;
	}

	

	public Long getTotalWorkMinutes() {
		return totalWorkMinutes;
	}

	public Long getTotalBreakMinutes() {
		return totalBreakMinutes;
	}


	public Long getPermissionMinutes() {
		return permissionMinutes;
	}

	
    
    
}

