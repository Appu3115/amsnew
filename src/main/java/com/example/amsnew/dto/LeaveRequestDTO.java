package com.example.amsnew.dto;

import java.time.LocalDate;

import com.example.amsnew.model.LeaveReason;
import com.example.amsnew.model.LeaveStatus;
import com.example.amsnew.model.LeaveType;

import jakarta.validation.constraints.NotNull;

public class LeaveRequestDTO {

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LeaveReason reason;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // getters & setters
    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveReason getReason() {
        return reason;
    }

    public void setReason(LeaveReason reason) {
        this.reason = reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}