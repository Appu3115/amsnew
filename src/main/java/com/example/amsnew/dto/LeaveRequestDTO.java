package com.example.amsnew.dto;

import java.time.LocalDate;

import com.example.amsnew.model.LeaveStatus;

public class LeaveRequestDTO {
    private Integer id;
    private String employeeId;
    private String employeeFirstName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
    private String reason;

    // Constructors
    public LeaveRequestDTO() {}

    public LeaveRequestDTO(Integer id, String employeeId, String employeeFirstName, LocalDate startDate,
                           LocalDate endDate, LeaveStatus status, String reason) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeFirstName = employeeFirstName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer integer) { this.id = integer; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeFirstName() { return employeeFirstName; }
    public void setEmployeeFirstName(String employeeFirstName) { this.employeeFirstName = employeeFirstName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus leaveStatus) { this.status = leaveStatus; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
