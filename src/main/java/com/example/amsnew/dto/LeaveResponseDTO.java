package com.example.amsnew.dto;

import java.time.LocalDate;

import com.example.amsnew.model.LeaveStatus;

public class LeaveResponseDTO {

    private Integer leaveId;
    private String employeeId;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;

    public LeaveResponseDTO(Integer leaveId,
                            String employeeId,
                            String employeeName,
                            LocalDate startDate,
                            LocalDate endDate,
                            LeaveStatus status) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LeaveStatus getStatus() {
        return status;
    }
}